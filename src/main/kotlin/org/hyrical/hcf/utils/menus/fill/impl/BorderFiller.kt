package org.hyrical.hcf.utils.menus.fill.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.menus.Menu
import org.hyrical.hcf.utils.menus.fill.IMenuFiller
import org.hyrical.hcf.utils.menus.page.PagedMenu

class BorderFiller : IMenuFiller {
    override fun fill(menu: Menu, player: Player, buttons: MutableMap<Int, Button>, size: Int) {
        val startIndex = if (menu is PagedMenu) 8 else 0
        for (i in startIndex until size) {
            if (i < startIndex + 9) {
                buttons.putIfAbsent(i, PlaceholderButton())
                buttons.putIfAbsent(i + (size - 9), PlaceholderButton())
            }
            if (i % 9 == 0) {
                buttons.putIfAbsent(i, PlaceholderButton())
                buttons.putIfAbsent(i + 8, PlaceholderButton())
            }
        }
    }

    class PlaceholderButton : Button() {
        override fun getItem(player: Player): ItemStack {
            return ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name(" ").build()
        }
    }
}