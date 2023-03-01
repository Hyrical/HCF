package org.hyrical.hcf.ability.damage

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.hyrical.hcf.ability.AbilityService
import org.hyrical.hcf.ability.AbilityDispatcher

object DamageAbilityDispatcher : AbilityDispatcher<EntityDamageByEntityEvent> {
    @EventHandler
    override fun dispatch(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player || event.entity !is Player) return
        val player = event.damager as Player

        AbilityService.damageAbilities.values.firstOrNull { it.getItemStack().isSimilar(player.itemInHand)}?.handle(event)
    }
}