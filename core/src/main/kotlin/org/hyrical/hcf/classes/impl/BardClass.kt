package org.hyrical.hcf.classes.impl

import com.cryptomorin.xseries.XMaterial
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.classes.ArmorClass
import org.hyrical.hcf.classes.impl.bard.BardEffect
import org.hyrical.hcf.utils.getProfile

class BardClass : ArmorClass("Bard", arrayListOf(
    XMaterial.GOLDEN_HELMET.parseMaterial()!!,
    XMaterial.GOLDEN_CHESTPLATE.parseMaterial()!!,
    XMaterial.GOLDEN_LEGGINGS.parseMaterial()!!,
    XMaterial.GOLDEN_BOOTS.parseMaterial()!!
)) {

    val HOLD_EFFECT_ITEMS = listOf(
        BardEffect(PotionEffectType.SPEED, 1, 1, Material.SUGAR),
        BardEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 1, Material.IRON_INGOT),
        BardEffect(PotionEffectType.INCREASE_DAMAGE, 0, 1, Material.BLAZE_POWDER),
        BardEffect(PotionEffectType.JUMP, 2, 1, Material.FEATHER)
    )

    val EFFECT_ITEMS = listOf(
        BardEffect(PotionEffectType.SPEED, 2, 5, Material.SUGAR),
        BardEffect(PotionEffectType.DAMAGE_RESISTANCE, 2, 5, Material.IRON_INGOT),
        BardEffect(PotionEffectType.INCREASE_DAMAGE, 1, 5, Material.BLAZE_POWDER),
        BardEffect(PotionEffectType.JUMP, 6, 5, Material.FEATHER)
    )

    val CONSTANT_EFFECT_LIST = listOf<Pair<PotionEffectType, Int>>(
        Pair(PotionEffectType.DAMAGE_RESISTANCE, 1),
        Pair(PotionEffectType.REGENERATION, 0),
        Pair(PotionEffectType.SPEED, 1)
    )
    override fun tick(player: Player) {
        //to fix bard speed issues
        for (effect in CONSTANT_EFFECT_LIST)
        {
            if (!player.hasPotionEffect(effect.first) || player.getPotionEffect(effect.first)!!.duration != Int.MAX_VALUE)
            {
                val p = effect.first
                val a = effect.second

                player.addPotionEffect(PotionEffect(p, Int.MAX_VALUE, a))
            }
        }

        val team = player.getProfile()?.team ?: return
        val holdingItem = HOLD_EFFECT_ITEMS.firstOrNull { player.inventory.itemInHand.isSimilar(ItemStack(it.material))} ?: return

        for (mate in team.mapToBukkitPlayer())
        {
            if (!mate.hasPotionEffect(holdingItem.type))
            {
                mate.addPotionEffect(PotionEffect(holdingItem.type, holdingItem.duration, holdingItem.amplifier))
            }
        }
    }

    override fun apply(player: Player) {
        for (effect in CONSTANT_EFFECT_LIST)
        {
            if (!player.hasPotionEffect(effect.first) || player.getPotionEffect(effect.first)!!.duration != Int.MAX_VALUE)
            {
                val p = effect.first
                val a = effect.second

                player.addPotionEffect(PotionEffect(p, Int.MAX_VALUE, a))
            }
        }
    }
}