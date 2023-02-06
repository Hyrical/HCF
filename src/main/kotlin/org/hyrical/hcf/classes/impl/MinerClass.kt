package org.hyrical.hcf.classes.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.classes.ArmorClass
import org.hyrical.hcf.config.impl.LangFile


class MinerClass : ArmorClass("Miner") {
    override fun apply(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, Int.MAX_VALUE, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 0))
    }

    override fun tick(player: Player) {
        if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
            player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 0))
        }

        if (player.location.y <= HCFPlugin.instance.config.getInt("CLASS.MINER-HEIGHT")){
            if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)){
                player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 5 * 20, 0))
            }
        }
    }

    override fun qualifies(armor: PlayerInventory): Boolean {
        return wearingAllArmor(armor) &&
                armor.helmet!!.type == Material.IRON_HELMET &&
                armor.chestplate!!.type == Material.IRON_CHESTPLATE &&
                armor.leggings!!.type == Material.IRON_LEGGINGS &&
                armor.boots!!.type == Material.IRON_BOOTS
    }
}