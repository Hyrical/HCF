package org.hyrical.hcf.profile.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.entity.Player
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.registry.annotations.Command
import org.hyrical.hcf.utils.translate

@Command
object PayCommand : BaseCommand() {

    @CommandAlias("pay|send|p2p")
    fun pay(sender: Player, target: Player, amount: Int) {
        val senderProfile = ProfileService.getProfile(sender.uniqueId) ?: return
        val targetProfile = ProfileService.getProfile(target.uniqueId) ?: return

        if (senderProfile.balance < amount) {
            sender.sendMessage(translate("&cYou do not have enough money to send."))
            return
        }

        senderProfile.balance -= amount
        targetProfile.balance += amount

        sender.sendMessage(translate("&eYou have sent &c$$amount &eto &c${target.name}&e."))
        target.sendMessage(translate("&eYou have received &c$$amount &efrom &c${sender.name}&e."))

        ProfileService.save(senderProfile)
        ProfileService.save(targetProfile)
    }
}