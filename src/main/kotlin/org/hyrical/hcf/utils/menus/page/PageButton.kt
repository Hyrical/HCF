package org.hyrical.hcf.utils.menus.page

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.misc.CC

class PageButton(private val mod: Int, private val menu: PagedMenu) : Button() {
    override fun getItem(player: Player): ItemStack {
        return if (hasNext(player)) {
            ItemBuilder(Material.GRAY_CARPET)
                .name(CC.translate(if (mod == 0) "&7Previous Page" else "&7Next page"))
                .amount(1)
                .data(7)
                .build()
        } else {
            ItemBuilder(Material.GRAY_CARPET)
                .name(CC.translate(if (mod == 0) "&7Previous Page" else "&7Next page"))
                .amount(1)
                .data(7)
                .build()
        }
    }

    override fun click(player: Player, slot: Int, clickType: ClickType, hotbarButton: Int) {
        if (clickType.isShiftClick) {
            if (hasNext(player)) {
                menu.modPage(player, if (mod > 0) menu.getPages(player) - menu.page else 1 - menu.page)
            }
        } else {
            if (hasNext(player)) {
                menu.modPage(player, mod)
            }
        }
    }

    private fun hasNext(player: Player): Boolean {
        val pg = menu.page + mod
        return pg > 0 && menu.getPages(player) >= pg
    }
}