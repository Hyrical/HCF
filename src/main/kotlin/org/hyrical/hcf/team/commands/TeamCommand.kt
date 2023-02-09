package org.hyrical.hcf.team.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Optional
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.team.Team
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

    @CommandAlias("who|i||info|")
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

                //team.sendTeamInformation(player)
            }
        }.runTaskAsynchronously(HCFPlugin.instance)
    }

}
