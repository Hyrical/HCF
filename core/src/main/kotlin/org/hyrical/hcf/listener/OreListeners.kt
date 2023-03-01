package org.hyrical.hcf.listener

import com.google.common.collect.ImmutableSet
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.metadata.FixedMetadataValue
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate

object OreListeners : Listener {
    val CHECK_FACES = listOf(
        BlockFace.NORTH,
        BlockFace.SOUTH,
        BlockFace.EAST,
        BlockFace.WEST,
        BlockFace.NORTH_EAST,
        BlockFace.NORTH_WEST,
        BlockFace.SOUTH_EAST,
        BlockFace.SOUTH_WEST,
        BlockFace.UP,
        BlockFace.DOWN
    )

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.type == Material.DIAMOND_ORE) {
            event.block.setMetadata("DiamondPlaced", FixedMetadataValue(HCFPlugin.instance, true))
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.block.type == Material.DIAMOND_ORE && !event.block.hasMetadata("DiamondPlaced")) {
            var diamonds = countRelative(event.block)

            val playerTeam = event.player.getProfile()!!.team
            playerTeam?.let {
                it.diamondsMined += diamonds
            }

            playerTeam?.save()

            Bukkit.broadcastMessage(translate(LangFile.getString("SERVER.DIAMOND-FOUND")!!.replace("%player%", event.player.name).replace("%diamonds%", diamonds.toString())))
        }
    }

    fun countRelative(block: Block): Int {
        var diamonds = 1
        block.setMetadata("DiamondPlaced", FixedMetadataValue(HCFPlugin.instance, true))

        for (checkFace in CHECK_FACES) {
            val relative = block.getRelative(checkFace)

            if (relative.type == Material.DIAMOND_ORE && !relative.hasMetadata("DiamondPlaced")) {
                relative.setMetadata("DiamondPlaced", FixedMetadataValue(HCFPlugin.instance, true))
                diamonds += countRelative(relative)
            }
        }

        return diamonds
    }
}