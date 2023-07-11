package org.hyrical.hcf.classes

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

abstract class ArmorClass(val name: String, val armor: ArrayList<Material>) : Listener {

    abstract fun tick(player: Player)
    abstract fun apply(player: Player)

    abstract fun remove(player: Player)

    fun isWearing(inventory: PlayerInventory): Boolean {
        val helmet: ItemStack? = inventory.helmet
        val chestplate: ItemStack? = inventory.chestplate
        val leggings: ItemStack? = inventory.leggings
        val boots: ItemStack? = inventory.boots

        return helmet != null && chestplate != null && leggings != null && boots != null && helmet.type == armor[0] &&
                chestplate.type == armor[1] && leggings.type == armor[2] && boots.type == armor[3]
    }

    fun removeEffects(player: Player) {
        for (effect in player.activePotionEffects){
            if (effect.duration < 1000000) continue // We do this so they dont loose like strength two for example, just their current armor class effects.

            player.removePotionEffect(effect.type)
        }
    }

}