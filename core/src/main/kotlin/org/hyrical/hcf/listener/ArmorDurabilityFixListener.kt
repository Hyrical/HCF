package org.hyrical.hcf.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent
import org.hyrical.hcf.HCFPlugin

object ArmorDurabilityFixListener : Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun damageEvent(event: PlayerItemDamageEvent){
        if (30 < HCFPlugin.RANDOM.nextInt(100)){
            event.isCancelled = true
            event.player.updateInventory()
        } // 30% chance for the item not to break/take durability
    }
}