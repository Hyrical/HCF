package org.hyrical.hcf.sign

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.utils.translate

object FundamentalClickableSignListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val block = event.clickedBlock ?: return

        if (!block.type.toString().contains("SIGN")) return
        val state = block.state as Sign
        FundamentalClickableSignCommunicator.getCustomSign(state.lines.toList())?.onClick(event)
    }

    @EventHandler
    fun onSignChange(event: SignChangeEvent) {
        val sign = event.block.state as Sign
        val selectSign = FundamentalClickableSignCommunicator.getCustomSign(sign.lines.map { ChatColor.stripColor(it)!! }) ?: return

        if (selectSign.requiresOP() && !event.player.isOp) return

        var i = 0

        selectSign.getLines().forEach {
            sign.setLine(i, translate(it))
            i++
        }
    }
}