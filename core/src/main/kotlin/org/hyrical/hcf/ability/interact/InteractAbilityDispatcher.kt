package org.hyrical.hcf.ability.interact

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.ability.AbilityService
import org.hyrical.hcf.ability.AbilityDispatcher

object InteractAbilityDispatcher : AbilityDispatcher<PlayerInteractEvent> {

    @EventHandler
    override fun dispatch(event: PlayerInteractEvent) {
        val player = event.player
        Bukkit.broadcastMessage("Entered dispatcher")
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        Bukkit.broadcastMessage("Starting to call it")
        AbilityService.interactAbilities.values.firstOrNull { it.getItemStack().isSimilar(player.itemInHand) }.let {
            it?.handle(event)

        }

    }
}