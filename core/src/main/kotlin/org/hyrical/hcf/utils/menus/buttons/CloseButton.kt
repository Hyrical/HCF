package org.hyrical.hcf.utils.menus.buttons

import com.cryptomorin.xseries.XMaterial
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.menus.Button

class CloseButton : Button() {

    override fun getItem(player: Player): ItemStack {
        return ItemBuilder.of(XMaterial.RED_BED.parseMaterial()!!)
            .name("&c&lClose")
            .build()
    }

    override fun click(player: Player, slot: Int, clickType: ClickType, hotbarButton: Int) {
        player.closeInventory()
    }
}