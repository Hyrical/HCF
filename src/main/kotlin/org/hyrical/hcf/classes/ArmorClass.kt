package org.hyrical.hcf.classes

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.PlayerInventory


abstract class ArmorClass(val name: String) : Listener {

    abstract fun apply(player: Player)
    abstract fun tick(player: Player)
    abstract fun qualifies(armor: PlayerInventory): Boolean

    open fun removeInfiniteEffects(player: Player) {
        for (potionEffect in player.activePotionEffects) {
            if (potionEffect.duration > 1000000) {
                player.removePotionEffect(potionEffect.type)
            }
        }
    }

    open fun wearingAllArmor(armor: PlayerInventory): Boolean {
        return armor.helmet != null && armor.chestplate != null && armor.leggings != null && armor.boots != null
    }

}