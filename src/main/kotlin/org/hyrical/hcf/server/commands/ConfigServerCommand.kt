package org.hyrical.hcf.server.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player
import org.hyrical.hcf.registry.annotations.Command

@Command
object ConfigServerCommand : BaseCommand() {

    @CommandAlias("configserver|cs")
    @CommandPermission("hcf.command.configserver")
    fun configServer(player: Player) {

    }
}