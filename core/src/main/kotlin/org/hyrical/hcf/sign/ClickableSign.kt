package org.hyrical.hcf.sign

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent

interface ClickableSign {

    fun getLines(): ArrayList<String>
    fun onClick(event: PlayerInteractEvent)

    fun getStripped(index: Int): String {
        return ChatColor.stripColor(getLines()[index])!!
    }
}