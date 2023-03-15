package org.hyrical.hcf.team.listeners

import com.cryptomorin.xseries.XMaterial
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Door
import org.bukkit.block.data.type.TrapDoor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate
import java.util.stream.Collectors


object ClaimPreventionListener : Listener {

    private val NO_INTERACT_WITH = mutableListOf<XMaterial>(XMaterial.LAVA_BUCKET, XMaterial.WATER_BUCKET, XMaterial.BUCKET)
    private val NO_INTERACT = mutableListOf(XMaterial.WARPED_FENCE_GATE, XMaterial.SPRUCE_FENCE_GATE, XMaterial.ACACIA_FENCE_GATE, XMaterial.BAMBOO_FENCE_GATE, XMaterial.BIRCH_FENCE_GATE, XMaterial.CRIMSON_FENCE_GATE, XMaterial.MANGROVE_FENCE_GATE, XMaterial.OAK_FENCE_GATE, XMaterial.FURNACE, XMaterial.BREWING_STAND, XMaterial.CHEST, XMaterial.HOPPER, XMaterial.DISPENSER, XMaterial.LEVER, XMaterial.DROPPER, XMaterial.ENCHANTING_TABLE, XMaterial.ANVIL, XMaterial.BEACON) // Parse these in a function

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player

        val teamAtLocation = TeamManager.getTeamAtLocation(event.player.location)

        if (teamAtLocation != null && !teamAtLocation.isMember(player.uniqueId) || ServerHandler.isWarzone(event.player.location)
            || teamAtLocation != null && !teamAtLocation.isRaidable()) {
            event.isCancelled = true
            player.sendMessage(translate(LangFile.getString("TEAM.PREVENTION.BUILD")!!.replace("%team%", ServerHandler.getTeamDisplayName(player, player.location))))
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun placeBlock(event: BlockPlaceEvent){
        val player = event.player

        if (TeamManager.getTeamAtLocation(player.location) != null || ServerHandler.isWarzone(player.location)){
            val teamAtLocation = TeamManager.getTeamAtLocation(player.location)

            if (teamAtLocation != null && teamAtLocation.isMember(player.uniqueId)
                || teamAtLocation != null && teamAtLocation.isRaidable()) return

            event.isCancelled = true
            player.sendMessage(translate(LangFile.getString("TEAM.PREVENTION.PLACE")!!.replace("%team%", ServerHandler.getTeamDisplayName(player, player.location))))
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun interact(event: PlayerInteractEvent){
        val player = event.player

        if (event.item != null && (event.action != Action.RIGHT_CLICK_BLOCK)) return

        val clickedBlock = event.clickedBlock ?: return

        if (TeamManager.getTeamAtLocation(player.location) != null){
            val teamAtLocation = TeamManager.getTeamAtLocation(player.location)!!

            if (teamAtLocation.isMember(player.uniqueId)) return
            if (teamAtLocation.isRaidable()) return

            if (getNoInteract().contains(clickedBlock.type) || getNoInteractWith().contains(clickedBlock.type)){
                event.isCancelled = true
                player.sendMessage(translate(LangFile.getString("TEAM.PREVENTION.INTERACT")!!.replace("%team%", teamAtLocation.getFormattedTeamName(player))))
            }

            if (event.action == Action.PHYSICAL) {
                event.isCancelled = true;
            }
         }
    }

    private fun getNoInteract(): MutableList<Material>{
        return NO_INTERACT.stream().map { it.parseMaterial()!! }.collect(Collectors.toList())
    }

    private fun getNoInteractWith(): MutableList<Material>{
        return NO_INTERACT_WITH.stream().map { it.parseMaterial()!! }.collect(Collectors.toList())
    }
}