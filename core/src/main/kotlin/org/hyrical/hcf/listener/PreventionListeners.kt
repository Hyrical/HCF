package org.hyrical.hcf.listener

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.hyrical.hcf.api.teams.TeamHandlerImpl
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.teams.TeamHandler

object PreventionListeners : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player

        val teamAtLocation = TeamManager.getTeamAtLocation(event.player.location)

        if (teamAtLocation != null && !teamAtLocation.isMember(player.uniqueId) || ServerHandler.isWarzone(event.player.location)) {
            // send cancel msg TODO: Embry
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onOpen(event: InventoryOpenEvent) {
        val inventory = event.inventory
        val player = event.player

        if (player.isOp && player.gameMode == GameMode.CREATIVE) return

        when (inventory.type) {
            InventoryType.ANVIL, InventoryType.MERCHANT -> {
                event.isCancelled = true
            }

            else -> {

            }
        }
    }
}