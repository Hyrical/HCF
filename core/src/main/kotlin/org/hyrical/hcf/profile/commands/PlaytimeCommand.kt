package org.hyrical.hcf.profile.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Optional
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.registry.annotations.Command
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate

@Command
object PlaytimeCommand : BaseCommand() {

    @CommandAlias("playtime|pt")
    fun playtime(sender: Player, @Optional target: Player?) {
        val targetProfile = if (target == null) {
            HCFPlugin.instance.profileService.getProfile(sender.uniqueId)
        } else {
            HCFPlugin.instance.profileService.getProfile(target.uniqueId)
        } ?: return

        val playtime = targetProfile.playtime
        val formattedPlaytime = TimeUtils.formatIntoDetailedString((playtime / 1000).toInt())

        sender.sendMessage(translate("&d${target?.name ?: sender.name}&e's playtime is &d$formattedPlaytime&e."))
    }
}