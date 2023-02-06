package org.hyrical.hcf.team

import org.bukkit.Location
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.team.user.TeamUser
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
            // remove this team from allied teams arraylists. Need TeamService first.
        }

        // Everyone is added to the members list.
        for (uuid in members){
            // turn their chat modes to public
        }

        // remove team from cache/mongo

    }

}