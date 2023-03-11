package org.hyrical.hcf.team.claim.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.team.claim.LandBoard
import org.hyrical.hcf.utils.items.ItemBuilder

object ClaimListener : Listener {
    val WAND_ITEM = LandBoard.generateClaimItem()

    @EventHandler
    fun claim(event: PlayerInteractEvent)
    {
        val player = event.player
        val inHand = player.inventory.itemInHand

        if (inHand == null || inHand.type == Material.AIR) return

        if (inHand.isSimilar(WAND_ITEM))
        {
            //TODO: Just set locations and track thats all :D
        }
    }
}