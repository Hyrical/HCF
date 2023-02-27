package org.hyrical.hcf.team.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.commands.annotation.Optional
import mkremins.fanciful.FancyMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.claim.cuboid.Cuboid
import org.hyrical.hcf.team.user.TeamRole
import org.hyrical.hcf.team.user.TeamUser
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate
import java.util.*


@CommandAlias("team|t|faction|f")
object TeamCommand : BaseCommand() {

    @HelpCommand
    fun help(player: Player){
        for (line in LangFile.getStringList("TEAM.TEAM-HELP")){
            player.sendMessage(translate(line))
        }
    }

    @Subcommand("who|i|info")
    fun who(player: Player, @Optional teamInput: Team?){
        object : BukkitRunnable() {
            override fun run() {
                val profile = player.getProfile()!!

                if (teamInput == null){
                    val team = profile.team

                    if (team == null){
                        player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))
                        return
                    }
                }

                val team = teamInput ?: profile.team

                team!!.sendTeamInformation(player)
            }
        }.runTaskAsynchronously(HCFPlugin.instance)
    }

    @Subcommand("create")
    fun create(player: Player, @Name("name") name: String){
        if (TeamManager.getTeam(name) != null) return player.sendMessage(translate(LangFile.getString("TEAM.TEAM-ALREADY-EXISTS")!!))

        val user = TeamUser(player.uniqueId, TeamRole.LEADER)
        val team = Team(name.lowercase(), name, user,
            members = mutableListOf(user))

        TeamManager.create(team)

        val profile = player.getProfile()!!

        profile.teamString = team.identifier
        profile.save()

        player.sendMessage(translate(LangFile.getString("TEAM.TEAM-CREATED-INFO")!!))
        Bukkit.broadcastMessage(translate(LangFile.getString("TEAM.TEAM-CREATE")!!
            .replace("%name%", name).replace("%player%", player.displayName)))
    }

    @Subcommand("disband")
    fun disband(player: Player){
        if (player.getProfile()!!.teamString == null) return player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))

        val profile = player.getProfile()!!
        val team = profile.team!!

        if (!team.isLeader(player.uniqueId)) return player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!
            .replace("%role%", "Leader")))

        team.disband()
        player.getProfile()!!.teamString = null

        team.sendTeamMessage(translate(LangFile.getString("TEAM.DISBAND-TEAM-MSG")!!))
        Bukkit.broadcastMessage(translate(LangFile.getString("TEAM.DISBAND")!!.replace("%name%", team.name).replace("%player%", player.name)))
    }

    @Subcommand("invite|inv")
    fun invite(player: Player, @Name("player") target: OfflinePlayer){
        if (player.getProfile()!!.teamString == null) return player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))

        val targetProfile = target.getProfile()!!
        val playerTeam = player.getProfile()!!.team!!

        if (playerTeam.members.size >= ServerHandler.maxFactionSize){
            player.sendMessage(translate(LangFile.getString("TEAM.MAX-FACTION-SIZE")!!
                .replace("%maxSize%", ServerHandler.maxFactionSize.toString())))
            return
        }

        if (!playerTeam.isCaptain(player.uniqueId) || !playerTeam.isCoLeader(player.uniqueId) || !playerTeam.isLeader(player.uniqueId)){
            return player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!
                .replace("%role%", "Captain")))
        }

        if (playerTeam.isMember(player.uniqueId)){
            return player.sendMessage(translate(LangFile.getString("ALREADY-IN-TEAM")!!.replace("%player%", target.name!!)))
        }

        if (targetProfile.invitations.contains(playerTeam.identifier)){
            return player.sendMessage(translate(LangFile.getString("TEAM.ALREADY-INVITED")!!))
        }

        targetProfile.invitations.add(playerTeam.identifier)
        targetProfile.save()

        val bukkitTarget = Bukkit.getPlayer(target.uniqueId)

        if (bukkitTarget != null){
            val message = FancyMessage(LangFile.getString("TEAM.TEAM-INVITED.MESSAGE")!!
                .replace("\\n", "\n").replace("%team%", playerTeam.name))

            message.tooltip(translate(LangFile.getString("TEAM.TEAM-INVITED.TOOLTIP")!!))
            message.command(translate(LangFile.getString("TEAM.TEAM-INVITED.COMMAND")!!))

            message.send(bukkitTarget)
        }
    }

    @CommandAlias("claim")
    fun claim(player: Player){
        val team = player.getProfile()!!.team
        val location = Location(player.world, player.location.x + 20, player.location.y + 356, player.location.z + 20)

        team!!.claims.add(Cuboid(player.location, location))
    }

    @CommandAlias("c|chat")
    fun chat(player: Player, @Optional )

}
