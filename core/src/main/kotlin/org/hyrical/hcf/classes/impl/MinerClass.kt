package org.hyrical.hcf.classes.impl

import com.cryptomorin.xseries.XMaterial
import com.cryptomorin.xseries.XPotion
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.classes.ArmorClass
import org.hyrical.hcf.config.impl.ClassFile
import org.hyrical.hcf.utils.plugin.PluginUtils

class MinerClass : ArmorClass("Miner", arrayListOf(
    XMaterial.IRON_HELMET.parseMaterial()!!,
    XMaterial.IRON_CHESTPLATE.parseMaterial()!!,
    XMaterial.IRON_LEGGINGS.parseMaterial()!!,
    XMaterial.IRON_BOOTS.parseMaterial()!!,
)), Runnable {

    init {
        Bukkit.getScheduler().runTaskTimer(HCFPlugin.instance, this, 20L, 20L)
    }

    override fun tick(player: Player) {}

    override fun apply(player: Player) {
        player.addPotionEffect(PotionEffect(XPotion.FAST_DIGGING.potionEffectType!!, Int.MAX_VALUE, 1))
        player.addPotionEffect(PotionEffect(XPotion.NIGHT_VISION.potionEffectType!!, Int.MAX_VALUE, 0))
        player.addPotionEffect(PotionEffect(XPotion.FIRE_RESISTANCE.potionEffectType!!, Int.MAX_VALUE, 0))
    }

    override fun remove(player: Player) { }

    override fun run() {
        for (player in PluginUtils.getOnlinePlayers()){
            if (!isWearing(player.inventory)) continue
            if (player.location.y <= ClassFile.getInt("MINER-HEIGHT") && player.world.environment == World.Environment.NORMAL){
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) continue

                player.addPotionEffect(PotionEffect(XPotion.INVISIBILITY.potionEffectType!!, Int.MAX_VALUE, 0))
            } else {
                if (player.hasPotionEffect(XPotion.INVISIBILITY.potionEffectType!!)){
                    player.removePotionEffect(XPotion.INVISIBILITY.potionEffectType!!)
                }
            }
        }
    }

}