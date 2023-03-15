package org.hyrical.hcf.listener

import com.cryptomorin.xseries.XMaterial
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

object FDListener : Listener {

    val CHECK_FACES: Set<BlockFace> = ImmutableSet.of(
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
    fun placeBlock(event: BlockPlaceEvent){
        if (event.block.type == Material.DIAMOND_ORE){
            event.block.setMetadata("DiamondPlaced", FixedMetadataValue(HCFPlugin.instance, true))
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun blockBreak(event: BlockBreakEvent){
        if (event.block.type == Material.DIAMOND_ORE && !event.block.hasMetadata("DiamondPlaced")){

            val player = event.player
            val profile = event.player.getProfile() ?: return

            val diamondsAround = getAllDiamondsAroundOne(event.block)
            if (profile.teamString != null){
                profile.team!!.diamondsMined += diamondsAround
                profile.team!!.save()
            }

            for (user in Bukkit.getOnlinePlayers()){
                if (profile.hasDiamondsOn){
                    user.sendMessage(translate(LangFile.getString("DIAMOND-FOUND")!!.replace("%player%", player.name).replace("%diamonds%", diamondsAround.toString())))
                }
            }
        }
    }

    private fun getAllDiamondsAroundOne(block: Block): Int {
        var diamonds = 1
        block.setMetadata("DiamondPlaced", FixedMetadataValue(HCFPlugin.instance, true)) // idea taken from HCTeams

        for (face in CHECK_FACES){
            val rl = block.getRelative(face)

            if (rl.type == XMaterial.DIAMOND_ORE.parseMaterial()!! && !rl.hasMetadata("DiamondPlaced")){
                rl.setMetadata("DiamondPlaced", FixedMetadataValue(HCFPlugin.instance, true))
                diamonds += getAllDiamondsAroundOne(rl) // recursive
            }
        }
        return diamonds
    } // best function name
}