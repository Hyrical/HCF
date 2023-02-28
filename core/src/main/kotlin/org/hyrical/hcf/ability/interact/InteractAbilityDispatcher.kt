package org.hyrical.hcf.ability.interact

import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.ability.AbilityService
import org.hyrical.hcf.ability.Dispatcher

object InteractAbilityDispatcher : Dispatcher<PlayerInteractEvent> {
    override fun dispatch(event: PlayerInteractEvent) {
        val player = event.player
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        AbilityService.interactAbilities.values.firstOrNull { it.getItemStack().isSimilar(player.itemInHand)}?.handle(event)
    }
}