package org.hyrical.hcf.sign

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.utils.translate

interface ClickableSign {

    fun getLines(): ArrayList<String>
    fun onClick(event: PlayerInteractEvent)

    fun requiresOP(): Boolean

    fun getStripped(): List<String> {
        return getLines().map { translate(it) }.map { ChatColor.stripColor(it)!! }
    }
}