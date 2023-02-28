package org.hyrical.hcf.ability.interact.impl

import com.cryptomorin.xseries.XMaterial
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.ability.interact.InteractAbility
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.translate

class PortableBardAbility(override val id: String, val itemName: String, val itemDescription: String, val xmaterial: XMaterial, val effectType: PotionEffectType, val amplifier: Int, val duration: Int) : InteractAbility(id) {
    override fun handle(event: PlayerInteractEvent) {
        val potion = PotionEffect(effectType, duration * 20, amplifier)
        // TODO: Add to team
        event.player.addPotionEffect(potion)
    }

    override fun getName(): String {
        return itemName
    }

    override fun getDescription(): String {
        return itemDescription
    }

    override fun getItemStack(): ItemStack {
        return ItemBuilder.of(xmaterial.parseMaterial()!!)
            .name(translate(itemName))
            .setLore(itemDescription.split("%nl%"))
            .build()
    }
}