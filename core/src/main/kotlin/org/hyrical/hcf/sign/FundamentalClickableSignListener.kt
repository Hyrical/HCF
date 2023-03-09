package org.hyrical.hcf.sign

import com.cryptomorin.xseries.XMaterial
import org.bukkit.ChatColor
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object FundamentalClickableSignListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock ?: return

        if (!block.type.toString().contains("SIGN")) return
        val state = block.state as Sign
        FundamentalClickableSignCommunicator.getCustomSign(state.lines.map {  ChatColor.stripColor(it)!! })?.onClick(event)
    }
}