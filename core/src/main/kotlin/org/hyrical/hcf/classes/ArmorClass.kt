package org.hyrical.hcf.classes

import com.google.common.collect.HashBasedTable
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*


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

    open fun smartAddPotion(
        player: Player?,
        potionEffect: PotionEffect?,
        persistOldValues: Boolean,
        pvpClass: ArmorClass?
    ) {
        setRestoreEffect(player!!, potionEffect!!)
    }


    private val restores: HashBasedTable<UUID, PotionEffectType, PotionEffect> = HashBasedTable.create()

    open fun setRestoreEffect(player: Player, effect: PotionEffect) {
        var shouldCancel = true
        val activeList = player.activePotionEffects
        for (active in activeList) {
            if (active.type != effect.type) continue

            // If the current potion effect has a higher amplifier, ignore this one.
            if (effect.amplifier < active.amplifier) {
                return
            } else if (effect.amplifier == active.amplifier) {
                // If the current potion effect has a longer duration, ignore this one.
                if (0 < active.duration && (effect.duration <= active.duration || effect.duration - active.duration < 10)) {
                    return
                }
            }
            restores.put(player.uniqueId, active.type, active)
            shouldCancel = false
            break
        }

        // Cancel the previous restore.
        player.addPotionEffect(effect, true)
        if (shouldCancel && effect.duration > 120 && effect.duration < 9600) {
            restores.remove(player.uniqueId, effect.type)
        }
    }
}