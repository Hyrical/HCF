package org.hyrical.hcf.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Name
import co.aikar.commands.annotation.Subcommand
import org.bukkit.entity.Player
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate

@CommandAlias("customtimer")
object CustomTimerCommand : BaseCommand() {

    val customTimers: HashMap<String, Long> = HashMap()

    @Subcommand("create")
    fun create(player: Player, @Name("time") timeString: String, @Name("name") name: String){
        val seconds = TimeUtils.parseTime(timeString)
        if (seconds < 0) return player.sendMessage(translate("&cInvalid time!"))

        if (customTimers[name] != null){
            player.sendMessage(translate("&cA custom timer with that identifier already exists."))
            return
        }

        customTimers[name] = System.currentTimeMillis() + (seconds * 1000)
    }

    @Subcommand("delete")
    fun delete(player: Player, @Name("name") name: String){
        if (customTimers[name] == null) return

        customTimers.remove(name)
    }


}