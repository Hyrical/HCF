package org.hyrical.hcf.sign

import org.bukkit.ChatColor
import org.bukkit.entity.Player

interface ClickableSign {

    fun getLines(): ArrayList<String>
    fun onClick(player: Player)

    fun getStripped(index: Int): String {
        return ChatColor.stripColor(getLines()[index])!!
    }
}