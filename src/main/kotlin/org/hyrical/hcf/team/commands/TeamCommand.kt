package org.hyrical.hcf.team.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.HelpCommand
import org.bukkit.entity.Player
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.utils.translate

@CommandAlias("team|t|faction|f")
object TeamCommand : BaseCommand() {

    @HelpCommand
    fun help(player: Player){
        for (line in LangFile.getStringList("TEAM.TEAM-HELP")){
            player.sendMessage(translate(line))
        }
    }


}