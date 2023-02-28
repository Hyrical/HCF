package org.hyrical.hcf.ability.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.entity.Player

object AbilitiesCommand : BaseCommand() {

    @CommandAlias("abilities")
    fun abilities(player: Player) {
        AbilitiesMenu().openMenu(player)
    }
}