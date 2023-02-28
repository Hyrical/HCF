package org.hyrical.hcf.walls

import com.cryptomorin.xseries.XMaterial
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.claim.cuboid.Cuboid
import org.hyrical.hcf.team.system.Flag
import org.hyrical.hcf.timer.type.impl.playertimers.CombatTimer
import org.hyrical.hcf.utils.plugin.PluginUtils
import java.util.*


class WallThread : Thread("Wall Thread") {

    private val cachedLocations = HashMap<UUID, MutableMap<Location, Long>>()

    override fun run() {
        while (true) {
            sleep(250L)
            showBordersToPlayers()
        }
    }

    fun showBordersToPlayers() {
        for (player in PluginUtils.getOnlinePlayers()) {
            val playerLocation = player.location
            val claims = getTeamsAndClaimsNearLocation(playerLocation)
                .filter { (_, cuboid) -> !cuboid.contains(playerLocation) }
                .map { it.value }
                .toList()

            if (claims.isEmpty()) {
                clearBordersForPlayer(player)
                continue
            }

            val cachedLocationsForPlayer = cachedLocations.getOrPut(player.uniqueId) { mutableMapOf() }
            val bordersIterator = cachedLocationsForPlayer.iterator()
            while (bordersIterator.hasNext()) {
                val (loc, timestamp) = bordersIterator.next()
                if (System.currentTimeMillis() >= timestamp) {
                    if (!loc.chunk.isLoaded) continue
                    player.sendBlockChange(loc, loc.block.type, loc.block.data)
                    bordersIterator.remove()
                    cachedLocationsForPlayer.remove(loc)
                }
            }

            for (claim in claims) {
                sendClaimToPlayer(player, claim)
            }
        }
    }


    private fun getTeamsAndClaimsNearLocation(location: Location): Map<Team, Cuboid> {
        val result = mutableMapOf<Team, Cuboid>()
        val locationWithoutY = Location(location.world, location.x, 0.0, location.z)
        for (team in TeamManager.cache.values) {
            for (cuboid in team.claims) {
                val cuboidLocation = Location(cuboid.world, cuboid.lowerX.toDouble(), 0.0, cuboid.lowerZ.toDouble())
                val distanceSquared = cuboidLocation.distanceSquared(locationWithoutY)
                if (distanceSquared <= 64) { // squared value of 8 blocks
                    result[team] = cuboid
                }
            }
        }
        return result
    }

    private fun clearBordersForPlayer(player: Player) {
        cachedLocations.remove(player.uniqueId)?.keys?.forEach {
            if (it.chunk.isLoaded) {
                player.sendBlockChange(it, it.block.type, it.block.data)
            }
        }
    }

    private fun hasFlag(team: Team, flag: Flag): Boolean {
        return team.factionType.contains(flag)
    }

    private fun sendClaimToPlayer(player: Player, claim: Cuboid) {
        val maxDistanceSquared = 64.0 // the maximum distance from the player that a block can be highlighted
        val borderMaterial = XMaterial.RED_STAINED_GLASS.parseMaterial() // the material to use for the border blocks
        val borderData = 14 // the data value to use for the border blocks
        val borderDuration = 4000L // the duration (in milliseconds) that the border blocks should remain visible

        // Get all the coordinates on the outer edge of the claim
        val edgeCoordinates = claim.getCoordinatesOnEdge()

        // Loop through each coordinate and highlight the blocks that are close to the player
        for ((x, z) in edgeCoordinates) {
            // Create a location for the block at this coordinate, at the same y-level as the player
            val blockLocation = Location(player.world, x.toDouble(), player.location.y, z.toDouble())

            // Calculate the distance from the player to this block
            val distanceSquared = player.location.distanceSquared(blockLocation)

            // If the block is within the maximum distance from the player, and is not solid, highlight it
            if (distanceSquared <= maxDistanceSquared && !blockLocation.block.type.isSolid) {
                // Send a block change packet to the player to make the block appear as the border material
                player.sendBlockChange(blockLocation, borderMaterial, borderData.toByte())

                // Add the location of the highlighted block to the cached locations map
                cachedLocations.computeIfAbsent(player.uniqueId) { mutableMapOf() }[blockLocation] = System.currentTimeMillis() + borderDuration
            }
        }
    }



}