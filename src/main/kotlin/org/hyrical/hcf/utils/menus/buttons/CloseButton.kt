package org.hyrical.hcf.utils.menus.buttons

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.menus.Button

class CloseButton : Button() {

    override fun getItem(player: Player): ItemStack {
        return ItemBuilder.of(Material.RED_BED)
            .name("&c&lClose")
            .build()
    }

    override fun click(player: Player, slot: Int, clickType: ClickType, hotbarButton: Int) {
        player.closeInventory()
    }
}