package org.hyrical.hcf.server.sotw.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Name
import co.aikar.commands.annotation.Subcommand
import org.bukkit.entity.Player
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.timer.type.impl.servertimers.SOTWTimer
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate

@CommandAlias("sotw")
object SOTWCommand : BaseCommand() {

    @Subcommand("start")
    @CommandPermission("hcf.sotw.start")
    fun sotwStart(player: Player, @Name("time") time: String){
        val seconds = TimeUtils.parseTime(time)

        if (seconds < 0) return player.sendMessage(translate("&cInvalid time!"))
        if (SOTWTimer.isSOTWActive()) return player.sendMessage(translate("&cSOTW is already active!"))

        SOTWTimer.start(System.currentTimeMillis() + (seconds * 1000)) // We need to times it by 1000 since it is seconds currently.
        player.sendMessage(translate("&aYou have started SOTW for &f" + TimeUtils.formatIntoMMSS(seconds)))
    }

    @Subcommand("spawn")
    fun sotwSpawn(player: Player){
        if (SOTWTimer.isSOTWActive()){
            player.teleport(ServerHandler.getSpawnLocation())
        } else {
            player.sendMessage(translate("&cYou cannot teleport to spawn while SOTW timer is not active."))
        }
    }

    @Subcommand("cancel|stop")
    @CommandPermission("hcf.sotw.start")
    fun sotwEnd(player: Player){
        if (!SOTWTimer.isSOTWActive()) return player.sendMessage(translate("&cSOTW is not active!"))

        SOTWTimer.timeRemaining = -1
        player.sendMessage(translate("&aYou have cancelled the SOTW timer."))
    }

    @Subcommand("enable")
    fun sotwEnable(player: Player){
        if (!SOTWTimer.isSOTWActive()) return player.sendMessage(translate("&cSOTW timer is not active!"))
        if (SOTWTimer.isSOTWEnabled(player)) return player.sendMessage(translate("You already have your SOTW timer enabled!"))

        SOTWTimer.sotw.add(player.uniqueId)
        player.sendMessage(translate("&aYou have enabled your SOTW timer."))
    }
}