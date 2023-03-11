package org.hyrical.hcf.ability.interact

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.ability.AbilityService
import org.hyrical.hcf.ability.AbilityDispatcher
import org.hyrical.hcf.timer.type.impl.playertimers.GlobalAbilityTimer

object InteractAbilityDispatcher : AbilityDispatcher<PlayerInteractEvent> {

    @EventHandler
    override fun dispatch(event: PlayerInteractEvent) {
        val player = event.player
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        val ability = AbilityService.interactAbilities.values.firstOrNull { it.getItemStack().isSimilar(player.itemInHand) } ?: return

        if (GlobalAbilityTimer.hasTimer(player)) {
            // TODO: Embry_ send msg
            return
        }

        if (AbilityService.cooldowns[ability.id]!![player.uniqueId] == null || AbilityService.cooldowns[ability.id]!![player.uniqueId]) // TODO: COoldown

        ability.handle(event)
    }
}