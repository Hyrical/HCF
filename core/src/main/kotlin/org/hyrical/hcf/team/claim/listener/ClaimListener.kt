package org.hyrical.hcf.team.claim.listener

import com.cryptomorin.xseries.XMaterial
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.utils.items.ItemBuilder

object ClaimListener : Listener {
    /*
    val WAND_ITEM =

    @EventHandler
    fun claim(event: PlayerInteractEvent)
    {
        val player = event.player
        val inHand = player.inventory.itemInHand
        val block = event.clickedBlock

        if (inHand.type == Material.AIR) return

        if (inHand.isSimilar(WAND_ITEM))
        {
            //TODO: Just set locations and track thats all :D
        }
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        if (!event.itemDrop.itemStack.isSimilar(WAND_ITEM)) return

        event.itemDrop.itemStack.type = XMaterial.AIR.parseMaterial() ?: return
    }

     */
}