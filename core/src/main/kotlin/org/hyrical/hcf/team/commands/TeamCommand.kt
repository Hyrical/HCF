package org.hyrical.hcf.team.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Name
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Subcommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.events.teams.TeamCreateEvent
import org.hyrical.hcf.events.teams.TeamDisbandEvent
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
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
        val user = TeamUser(player.uniqueId, TeamRole.LEADER)
        val team = Team(name.lowercase(), name, user,
            members = mutableListOf(user))

        TeamManager.create(team)

        val profile = player.getProfile()!!

        profile.teamString = team.identifier
        profile.save()

        player.sendMessage("creaTED")

        Bukkit.getPluginManager().callEvent(
            TeamCreateEvent(team.mapToAPI())
        )
    }

    @Subcommand("disband")
    fun disband(player: Player){
        val team = player.getProfile()!!.team

        team!!.disband()

        Bukkit.getPluginManager().callEvent(
            TeamDisbandEvent(team.mapToAPI(), reason = TeamDisbandEvent.Reason.CHOICE)
        )
    }

    @Subcommand("invite|inv")
    fun invite(player: Player, @Name("player") target: Player){

    }

}
