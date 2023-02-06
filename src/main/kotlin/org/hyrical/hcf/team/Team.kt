package org.hyrical.hcf.team

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.chat.mode.ChatMode
import org.hyrical.hcf.team.user.TeamRole
import org.hyrical.hcf.team.user.TeamUser
import org.hyrical.hcf.utils.getProfile
import org.hyrical.store.Storable
import java.text.DecimalFormat
import java.util.UUID

//\\ Persisted values \\//
class Team(
    override val identifier: String,
    var name: String,
    var leader: TeamUser,
    var members: MutableList<TeamUser> = mutableListOf(),
    var allies: MutableList<String> = mutableListOf(),
    var dtr: Double = 1.1,
    var kothCaptures: Int = 0,
    var citadelCaptures: Int = 0,
    var kills: Int = 0,
    var deaths: Int = 0,
    var balance: Double = 0.0,
    var friendlyFire: Boolean = false,
    var announcement: String = "",
    var claimLocked: Boolean = false,
    var hq: String? = null,
) : Storable {

    //\\ Not persisted \\//
    @Transient val DTR_FORMAT: DecimalFormat = DecimalFormat("0.0")
    @Transient var focusedTeam: Team? = null
    @Transient var rallyLocation: Location? = null

    @Transient var bards: Int = 0
    @Transient var rogues: Int = 0
    @Transient var archers: Int = 0

    fun calculatePoints(): Int {
        var total = 0
        val config = HCFPlugin.instance.config

        total += kills * config.getInt("POINTS.POINTS-KILL")
        total -= deaths * config.getInt("POINTS.POINTS-DEATH")
        total += kothCaptures * config.getInt("POINTS.POINTS-KOTH")
        total += citadelCaptures * config.getInt("POINTS.POINTS-CITADEL")

        return total
    }

    fun isRaidable(): Boolean {
        return dtr < 0
    }

    fun disband() {
        for (ally in allies){
            val allyTeam = TeamService.getTeam(ally)

            allyTeam?.allies?.remove(identifier)
        }

        // Everyone is added to the members list.
        for (user in members){
            // turn their chat modes to public
            Bukkit.getOfflinePlayer(user.uuid).getProfile()!!.chatMode  = ChatMode.PUBLIC
        }

        // remove team from cache/mongo
        TeamService.delete(this)
    }

    fun isLeader(uuid: UUID): Boolean {
        return leader.uuid == uuid
    }

    fun isCoLeader(uuid: UUID): Boolean {
        return members.any { it.uuid == uuid && it.role == TeamRole.COLEADER }
    }

    fun isCaptain(uuid: UUID): Boolean {
        return members.any { it.uuid == uuid && it.role == TeamRole.CAPTAIN }
    }

    fun isMember(uuid: UUID): Boolean {
        return members.any { it.uuid == uuid }
    }


    fun isInTeam(uuid: UUID): Boolean {
        return members.any { it.uuid == uuid }
    }

}