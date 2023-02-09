package org.hyrical.hcf.classes.impl.bard

import org.bukkit.potion.PotionEffect

class BardEffect private constructor(
    val potionEffect: PotionEffect?,
    val energy: Int
) {
    private val lastMessageSent: Map<String, Long> = HashMap()

    companion object {
        fun fromPotion(potionEffect: PotionEffect?): BardEffect {
            return BardEffect(potionEffect, -1)
        }

        fun fromPotionAndEnergy(potionEffect: PotionEffect?, energy: Int): BardEffect {
            return BardEffect(potionEffect, energy)
        }

        fun fromEnergy(energy: Int): BardEffect {
            return BardEffect(null, energy)
        }
    }
}