package org.hyrical.hcf.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player
import org.hyrical.hcf.timer.type.impl.playertimers.CombatTimer

object TagMeCommand : BaseCommand() {

    @CommandAlias("tagme")
    @CommandPermission("hcf.debug")
    fun tagme(player: Player) {
        CombatTimer.applyTimer(player)
    }
}