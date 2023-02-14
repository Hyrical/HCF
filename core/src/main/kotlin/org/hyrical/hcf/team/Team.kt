package org.hyrical.hcf.team

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.chat.mode.ChatMode
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.profile.Profile
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.serialize.LocationSerializer
import org.hyrical.hcf.team.dtr.DTRHandler
import org.hyrical.hcf.team.user.TeamRole
import org.hyrical.hcf.team.user.TeamUser
import org.hyrical.hcf.teams.HCFTeamRole
import org.hyrical.hcf.teams.HCFTeamUser
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate
import org.hyrical.store.Storable
import java.text.DecimalFormat
import java.text.NumberFormat
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
    var isRegenerating: Boolean = false,
    var invitations: MutableList<UUID> = mutableListOf(),
) : Storable {

    //\\ Not persisted \\//
    @Transient var focusedTeam: Team? = null
    @Transient var rallyLocation: Location? = null

    @Transient var bards: Int = 0
    @Transient var rogues: Int = 0
    @Transient var archers: Int = 0

    fun getDTRFormat(): DecimalFormat {
        return DecimalFormat("0.00")
    }

    fun calculatePoints(): Int {
        var total = 0

        val config = HCFPlugin.instance.config

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

    fun getRelationColor(player: Player): String {
        val config = HCFPlugin.instance.config

        return if (isInTeam(player.uniqueId)) config.getString("RELATION-COLOR.TEAMMATE")!! else config.getString("RELATION-COLOR.ENEMY")!!
    }

    fun getMaxDTR(): Double {
        val config = HCFPlugin.instance.config

        val dtrPerPlayer = members.size * config.getDouble("TEAM-DTR.PER-PLAYER")
        val minDTR = config.getDouble("TEAM-DTR.MAX-DTR")

        return min(dtrPerPlayer, minDTR)
    }

    fun isRegening(): Boolean {
        return DTRHandler.hasTimer(this)
    }

    fun sendTeamInformation(player: Player){
        for (line in LangFile.getStringList("TEAM.FACTION-INFORMATION.TEAM-INFO")){
            if (line.contains("&eCo-Leaders: &f%coleaders%")){
                if (members.none { it.role == TeamRole.COLEADER } ) continue

                player.sendMessage(line.replace("%coleaders%", getFormattedNamesByRole(TeamRole.COLEADER)))
                continue
            } else if (line.contains("&eCaptains: &f%captains%")){
                if (members.none { it.role == TeamRole.CAPTAIN } ) continue

                player.sendMessage(line.replace("%captins%", getFormattedNamesByRole(TeamRole.CAPTAIN)))
                continue
            } else if (line.contains("&eMembers: &f%members%")){
                if (members.none { it.role == TeamRole.MEMBER } ) continue

                player.sendMessage(line.replace("%members%", getFormattedNamesByRole(TeamRole.MEMBER)))
                continue
            }

            if (line.contains("&eTime until Regen: &9%regen%")){
                if (!isRegening()) continue

                player.sendMessage(translate(line.replace("%regen%", TimeUtils.formatIntoDetailedString(
                    (DTRHandler.getRemaining(this) / 1000L).toInt()))))
                continue
            }



            player.sendMessage(translate(line.replace(
                "%name%", getFormattedTeamName(player))
                .replace("%online%", members.count { Bukkit.getPlayer(it.uuid) != null }.toString())
                .replace("%max-online%", members.size.toString()).replace("%hq%",
                    getFormattedHQ())
                .replace("%dtr%", getDTRFormat().format(dtr))
                .replace("%color%", getDTRColor())
                .replace("%symbol%", getDTRSymbol())
                .replace("%points%", calculatePoints().toString())
                .replace("%kothcaptures%", kothCaptures.toString())
                .replace("%balance%", NumberFormat.getInstance().format(balance))
                .replace("%leader%", formatName(leader.uuid))))
        }
    }

    fun getFormattedHQ(): String {
        var hqString = "None"

        if (hq != null){
            val location = LocationSerializer.deserialize(hq!!)

            hqString = "${location.x}&7, &f${location.z}"
        }

        return hqString
    }

    fun getDTRColor(): String {
        val path = "TEAM-DTR"
        val config = HCFPlugin.instance.config

        return if (dtr <= 0.0) config.getString("$path.COLOR.RAIDABLE")!! else if (dtr < config.getDouble("$path.LOW-DTR")) config.getString("$path.COLOR.LOW-DTR")!! else config.getString("$path.COLOR.NORMAL")!!
    }

    fun getDTRSymbol(): String {
        val path = "TEAM-DTR"
        val config = HCFPlugin.instance.config

        return if (DTRHandler.hasTimer(this)) config.getString("$path.SYMBOL.DTR-FREEZE")!! else if (isRegenerating) config.getString("$path.SYMBOL.REGENERATING")!! else config.getString("$path.SYMBOL.NORMAL")!!
    }


    fun getFormattedTeamName(player: Player): String {
        val config = HCFPlugin.instance.config

        return if (player.getProfile()!!.team == this) config.getString("RELATION-COLOR.TEAMMATE")!! + name else config.getString("RELATION-COLOR.ENEMY")!! + name
    }

    private fun getFormattedNamesByRole(role: TeamRole): String {
        return members.filter { it.uuid != leader.uuid && it.role == role}.joinToString { formatName(it.uuid) }
    }

    private fun formatName(uuid: UUID): String {
        val player = Bukkit.getOfflinePlayer(uuid)
        val profile = ProfileService.getProfile(uuid)!!
        val kills = profile.kills

        return if (player.isOnline) LangFile.getString("TEAM.FACTION-INFORMATION.NAME-FORMAT.ONLINE")!!
            .replace("%player%", player.name!!).replace("%kills%", kills.toString()) else LangFile.getString("TEAM.FACTION-INFORMATION.NAME-FORMAT.OFFLINE")!!.replace("%kills%", kills.toString()).replace("%player%", player.name!!)
    }

    fun save(){
        TeamManager.save(this)
    }

    fun getOnlineMembers(): List<TeamUser> {
        val team: ArrayList<TeamUser> = arrayListOf()

        for (user in members){
            val player = Bukkit.getPlayer(user.uuid)

            if (player != null){
                team.add(user)
            }
        }

        return team.toList()
    }

    fun getUser(uuid: UUID): TeamUser? {
        return members.firstOrNull { it.uuid == uuid }
    }

    fun mapToAPI(): org.hyrical.hcf.teams.HCFTeam {
        return org.hyrical.hcf.teams.HCFTeam(
             identifier,
             name,
             HCFTeamUser(leader.uuid, HCFTeamRole.LEADER),
             members.map { HCFTeamUser(it.uuid, it.role.toAPI()) }.toMutableList(),
             allies,
             dtr,
             kothCaptures,
             citadelCaptures,
             kills,
             deaths,
             balance,
             friendlyFire,
             announcement,
             claimLocked,
             hq,
             isRegenerating,
             invitations
        )
    }
}