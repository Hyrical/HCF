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
        }

        if (target == null) {
            player.sendMessage(translate("&cYou have &e${targetProfile?.soulboundLives!! + targetProfile?.friendLives!!} &clives."))
        } else {
            player.sendMessage(translate("&c${target.name} has &e${targetProfile?.lives} &clives."))
        }
    }
}