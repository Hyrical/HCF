package org.hyrical.hcf.team

import org.bukkit.Bukkit
import org.bukkit.Location
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.chat.mode.ChatMode
import org.hyrical.hcf.profile.Profile
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.team.dtr.DTRHandler
import org.hyrical.hcf.team.user.TeamRole
import org.hyrical.hcf.team.user.TeamUser
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate
import org.hyrical.store.Storable
import java.text.DecimalFormat
import java.util.UUID
import java.util.concurrent.CompletableFuture
import kotlin.math.max
import kotlin.math.min

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
    var isRegening: Boolean = false,
) : Storable {

    //\\ Not persisted \\//
    @Transient val DTR_FORMAT: DecimalFormat = DecimalFormat("0.0")
    @Transient var focusedTeam: Team? = null
    @Transient var rallyLocation: Location? = null

    @Transient var bards: Int = 0
    @Transient var rogues: Int = 0
    @Transient var archers: Int = 0

    @Transient private val config = HCFPlugin.instance.config

    fun calculatePoints(): Int {
        var total = 0

        total += kills * config.getInt("POINTS.POINTS-KILL")
        total -= deaths * config.getInt("POINTS.POINTS-DEATH")
        total += kothCaptures * config.getInt("POINTS.POINTS-KOTH")
        total += citadelCaptures * config.getInt("POINTS.POINTS-CITADEL")

        return total
    }

    fun setDTR(dtr: Double) {
        if (dtr > getMaxDTR()){
            this.dtr = getMaxDTR()
        } else {
            this.dtr = max(dtr, HCFPlugin.instance.config.getDouble("TEAM-DTR.MIN-DTR"))
        }
    }

    fun isRaidable(): Boolean {
        return dtr < 0
    }

    fun disband() {
        for (ally in allies){
            val allyTeam = TeamManager.getTeam(ally)

            allyTeam?.allies?.remove(identifier)
        }

        // Everyone is added to the members list.
        for (user in members){
            // turn their chat modes to public
            Bukkit.getOfflinePlayer(user.uuid).getProfile()!!.chatMode  = ChatMode.PUBLIC
        }

        // remove team from cache/mongo
        TeamManager.delete(this)
    }

    fun sendTeamMessage(message: String){
        members.forEach {
            val player = Bukkit.getPlayer(it.uuid)

            if (player != null && player.isOnline){
                player.sendMessage(translate(message))
            }
        }
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

    fun getMembersAsProfiles(): CompletableFuture<List<Profile>> {
        return CompletableFuture.supplyAsync {
            members.mapNotNull { ProfileService.getProfile(it.uuid) }
        }
    }

    fun getMaxDTR(): Double {
        val dtrPerPlayer = members.size * config.getDouble("TEAM-DTR.PER-PLAYER")
        val minDTR = config.getDouble("TEAM-DTR.MAX-DTR")

        return min(dtrPerPlayer, minDTR)
    }

    fun isRegening(): Boolean {
        return DTRHandler.hasTimer(this)
    }

    fun save(){
        TeamManager.save(this)
    }
}