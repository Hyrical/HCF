package org.hyrical.hcf.utils.menus.fill.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.menus.Menu
import org.hyrical.hcf.utils.menus.fill.IMenuFiller
import org.hyrical.hcf.utils.menus.page.PagedMenu

class FillFiller : IMenuFiller {
    override fun fill(menu: Menu, player: Player, buttons: MutableMap<Int, Button>, size: Int) {
        for (i in (if (menu is PagedMenu) 8 else 0) until size) {
            buttons.putIfAbsent(
                i,
                PlaceholderButton()
            )
        }
    }

    class PlaceholderButton : Button() {
        override fun getItem(player: Player): ItemStack {
            return ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name(" ").build()
        }
    }
}