package org.hyrical.hcf.profile.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Optional
import org.bukkit.entity.Player
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.registry.annotations.Command
import org.hyrical.hcf.utils.translate

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
            sender.sendMessage(translate("&cYou have &e$$${targetProfile.balance} &cin your balance."))
        } else {
            sender.sendMessage(translate("&ePlayer &c${target.name} &ehas &e$$${targetProfile.balance} &ein their balance."))
        }
    }
}