package org.hyrical.hcf.utils.menus.buttons

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.menus.Menu
import org.hyrical.hcf.utils.misc.CC

class BackButton(val menu: Menu) : Button() {

    override fun getItem(player: Player): ItemStack {
        return ItemBuilder(Material.RED_BED).name((CC.RED) + "Go Back").build()
    }

    override fun click(player: Player, slot: Int, clickType: ClickType, hotbarButton: Int) {
        menu.openMenu(player)
    }
}