package org.hyrical.hcf.profile.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Name
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Subcommand
import org.bukkit.entity.Player
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.registry.annotations.Command
import org.hyrical.hcf.utils.translate

@Command
@CommandAlias("lives")
object LivesCommand : BaseCommand() {

    @HelpCommand
    fun help(player: Player) {
        player.sendMessage(translate("&7&m----------------------------"))
        player.sendMessage(translate("&c/lives check <player>"))
        player.sendMessage(translate("&c/lives revive <player>"))
        player.sendMessage(translate("&c/lives send <player> <amount>"))
        player.sendMessage(translate("&7&m----------------------------"))
    }

    @Subcommand("check")
    fun check(player: Player, @Optional @Name("target") target: Player?) {
        val targetProfile = if (target == null) {
            ProfileService.getProfile(player.uniqueId)
        } else {
            ProfileService.getProfile(target.uniqueId)
        } ?: return

        if (target == null) {
            player.sendMessage(translate("&cYou have &e${targetProfile.lives} &clives."))
        } else {
            player.sendMessage(translate("&ePlayer &c${target.name} &ehas &c${targetProfile.lives} &elives."))
        }
    }

    @Subcommand("send")
    fun send(player: Player, @Name("target") target: Player, @Name("amount") amount: Int) {
        if (amount <= 0) {
            player.sendMessage(translate("&cYou cannot send a negative amount of lives."))
            return
        }

        val profile = ProfileService.getProfile(player.uniqueId) ?: return
        val targetProfile = ProfileService.getProfile(target.uniqueId) ?: return

        if (profile.friendLives < amount) {
            player.sendMessage(translate("&cYou do not have enough lives to send."))
            return
        }

        profile.friendLives -= amount
        targetProfile.friendLives += amount

        player.sendMessage(translate("&eYou have sent &c$amount &elives to &c${target.name}&e."))
        target.sendMessage(translate("&eYou have received &c$amount &elives from &c${player.name}&e."))

        ProfileService.save(profile)
        ProfileService.save(targetProfile)
    }

    // TODO: rewvive 
}