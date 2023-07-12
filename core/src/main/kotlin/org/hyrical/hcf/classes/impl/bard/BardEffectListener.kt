package org.hyrical.hcf.classes.impl.bard

import com.cryptomorin.xseries.XMaterial
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.classes.ArmorClassHandler
import org.hyrical.hcf.classes.impl.BardClass
import org.hyrical.hcf.timer.type.impl.playertimers.BardEffectTimer
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate

/**
 * Class created on 7/12/2023

 * @author 98ping
 * @project HCF
 * @website https://solo.to/redis
 */
class BardEffectListener : Listener {

    @EventHandler
    fun clickBardEffect(e: PlayerInteractEvent) {
        val a = e.action
        val player = e.player

        if (!ArmorClassHandler.hasKitOn(player, ArmorClassHandler.armorClasses.first { it.name == "Bard" })) return

        val bard = ArmorClassHandler.getCurrentClass(player) as BardClass
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            val item = player.inventory.itemInHand

            if (item == null || item.type == XMaterial.AIR.parseMaterial()) return

            for (effect in bard.EFFECT_ITEMS) {
                val type = item.type

                if (effect.material == type) {
                    if (BardEffectTimer.hasTimer(player)) {
                        player.sendMessage(translate("&cYou are still on &eBard Effect &ccooldown"))
                        return
                    }

                    val amount = item.amount

                    if (amount == 1) {
                        player.setItemInHand(null)
                        player.updateInventory()
                    } else {
                        player.itemInHand.amount--
                        player.updateInventory()
                    }

                    val toGive = PotionEffect(effect.type, effect.duration * 20, effect.amplifier)

                    //so solo bard users cant strength 2
                    if (toGive.type != PotionEffectType.INCREASE_DAMAGE) {
                        player.addPotionEffect(toGive)
                    }

                    BardEffectTimer.applyTimer(player)

                    player.sendMessage(translate("&fYou have used the effect &b" + effect.type.name))

                    val team = player.getProfile()?.team ?: return

                    for (member in team.mapToBukkitPlayer())
                    {
                        if (member.uniqueId != player.uniqueId)
                        {
                            member.addPotionEffect(toGive)
                        }
                    }
                }
            }
        }
    }
}