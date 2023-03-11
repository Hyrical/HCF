package org.hyrical.hcf.team.claim.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.team.claim.LandGrid

object ClaimListener : Listener {
    val WAND_ITEM = LandGrid.generateClaimItem()

    @EventHandler
    fun claim(event: PlayerInteractEvent)
    {
        val player = event.player
        val inHand = player.inventory.itemInHand

        if (inHand == null || inHand.type == Material.AIR) return

        if (inHand.isSimilar(WAND_ITEM))
        {
            val action = event.action

            if (action == Action.LEFT_CLICK_BLOCK)
            {

            }
        }
    }
}