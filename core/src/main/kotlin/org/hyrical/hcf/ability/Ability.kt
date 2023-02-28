package org.hyrical.hcf.ability

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

abstract class Ability<T : Event>(open val id: String) {

    abstract fun getName(): String
    abstract fun getDescription(): String
    abstract fun getItemStack(): ItemStack

    abstract fun handle(event: T)

    fun removeItem(player: Player) {
        val item = player.itemInHand
        if (item.amount == 1) {
            player.setItemInHand(null)
        } else {
            item.amount -= 1
            player.setItemInHand(item)
        }

        player.updateInventory()
    }
}