package org.hyrical.hcf.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.entity.Player
import org.hyrical.hcf.timer.type.impl.playertimers.CombatTimer

object TagMeCommand : BaseCommand() {

    @CommandAlias("tagme")
    fun tagme(player: Player) {
        CombatTimer.applyTimer(player)
    }
}