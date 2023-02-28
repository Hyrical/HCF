package org.hyrical.hcf.classes.impl.bard

import org.bukkit.Material
import org.bukkit.potion.PotionEffectType

data class BardEffect(
    val type: PotionEffectType,
    val amplifier: Int,
    val duration: Int,
    val material: Material
)