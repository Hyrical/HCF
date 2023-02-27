package org.hyrical.hcf.profile.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Name
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Subcommand
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.registry.annotations.Command
import org.hyrical.hcf.utils.translate
import java.text.NumberFormat

@Command
@CommandAlias("lives")
object LivesCommand : BaseCommand() {

    @HelpCommand
    fun help(player: Player) {
        for (string in LangFile.getStringList("LIVES.LIVES-HELP")){
            player.sendMessage(translate(string))
        }
    }

    @Subcommand("check")
    fun check(player: Player, @Optional @Name("target") target: Player?) {
        val targetProfile = if (target == null) {
            HCFPlugin.instance.profileService.getProfile(player.uniqueId)
        } else {
            HCFPlugin.instance.profileService.getProfile(target.uniqueId)
        } ?: return

        if (target == null) {
            player.sendMessage(translate(LangFile.getString("LIVES.LIVES-CHECK.LIVES-SELF")!!.replace("%lives%", targetProfile.lives.toString())))
        } else {
            player.sendMessage(translate(LangFile.getString("LIVES.LIVES-CHECK.LIVES-TARGET")!!.replace("%target%", target.displayName).replace("%lives%", targetProfile.lives.toString())))
        }
    }

    @Subcommand("send")
    fun send(player: Player, @Name("target") target: Player, @Name("amount") amount: Int) {
        if (amount <= 0) {
            player.sendMessage(translate(LangFile.getString("LIVES.LIVES-SEND.NEGATIVE-AMOUNT")!!))
            return
        }

        val profile = HCFPlugin.instance.profileService.getProfile(player.uniqueId) ?: return
        val targetProfile = HCFPlugin.instance.profileService.getProfile(target.uniqueId) ?: return

        if (profile.friendLives < amount) {
            player.sendMessage(translate(LangFile.getString("LIVES.LIVES-SEND.NOT-ENOUGH")!!))
            return
        }

        profile.friendLives -= amount
        targetProfile.friendLives += amount

        player.sendMessage(translate(LangFile.getString("LIVES.LIVES-SEND.SEND-SELF")!!
            .replace("%target%", player.displayName).replace("%amount%", NumberFormat.getInstance().format(amount))))
        target.sendMessage(translate(LangFile.getString("LIVES.LIVES-SEND.SEND-TARGET")!!
            .replace("%player%", player.displayName).replace("%amount%", NumberFormat.getInstance().format(amount))))

        HCFPlugin.instance.profileService.save(profile)
        HCFPlugin.instance.profileService.save(targetProfile)
    }

    // TODO: rewvive 
}