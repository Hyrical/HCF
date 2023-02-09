package org.hyrical.hcf.profile.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Optional
import org.bukkit.entity.Player
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.registry.annotations.Command
import org.hyrical.hcf.utils.translate
import java.text.NumberFormat

@Command
object BalanceCommand : BaseCommand() {

    @CommandAlias("balance|bal|money")
    fun balance(sender: Player, @Optional target: Player?) {
        val targetProfile = if (target == null) {
            ProfileService.getProfile(sender.uniqueId)
        } else {
            ProfileService.getProfile(target.uniqueId)
        } ?: return

        if (target == null) {
            sender.sendMessage(translate(LangFile.getString("BALANCE.BALANCE-SELF")!!.replace("%balance%", NumberFormat.getInstance().format(targetProfile.balance))))
        } else {
            sender.sendMessage(translate(LangFile.getString("BALANCE.BALANCE-TARGET")!!.replace("%target%", target.displayName).replace("%balance%", NumberFormat.getInstance().format(targetProfile.balance))))
        }
    }
}