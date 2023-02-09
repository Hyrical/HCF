package org.hyrical.hcf.utils.menus.fill

import org.bukkit.entity.Player
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.menus.Menu

interface IMenuFiller {
    fun fill(menu: Menu, player: Player, buttons: MutableMap<Int, Button>, size: Int)
}