package org.hyrical.hcf.walls

import com.cryptomorin.xseries.XMaterial
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
            val claims = LinkedList<Cuboid>()
            val playerLocation = player.location
            for ((team, cuboid) in getTeamsAndClaimsNearLocation(playerLocation)) {
                if (cuboid.contains(playerLocation)) continue
                if (team.leader == null) {
                    if (hasFlag(team, Flag.SPAWN) && CombatTimer.hasTimer(player)) {
                        claims.add(cuboid)
                    } else if (hasFlag(team, Flag.KOTH) && false/* Check for pvp timer here */) {

                    }
                } else if (/* Check for pvp timer here */false) {
                    claims.add(cuboid)
                }
            }

            if (claims.isEmpty()) {
                clearBordersForPlayer(player)
                continue
            }

            val bordersIterator = cachedLocations.getOrPut(player.uniqueId) { mutableMapOf() }.iterator()
            while (bordersIterator.hasNext()) {
                val (loc, value) = bordersIterator.next()
                if (System.currentTimeMillis() >= value) {
                    if (!loc.chunk.isLoaded) continue
                    player.sendBlockChange(loc, loc.block.type, loc.block.data)
                    bordersIterator.remove()
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
        val edgeCoordinates = claim.getCoordinatesOnEdge()
        for (coordinate in edgeCoordinates) {
            val onPlayerY = Location(player.world, coordinate.first.toDouble(), player.location.y, coordinate.second.toDouble())

            // Ignore an entire pillar if the block closest to the player is further than the max distance (none of the others will be close enough, either)
            if (onPlayerY.distanceSquared(player.location) > 64) {
                continue
            }

            for (i in -4..4) {
                val check = onPlayerY.clone().add(0.0, i.toDouble(), 0.0)
                if (!check.block.type.isTransparent) continue
                if (check.world!!.isChunkLoaded(check.blockX shr 4, check.blockZ shr 4) && check.distanceSquared(onPlayerY) < 64) {
                    player.sendBlockChange(check, XMaterial.RED_STAINED_GLASS.parseMaterial()!!, 14) // Red stained glass
                    cachedLocations.computeIfAbsent(player.uniqueId) { mutableMapOf() }[check] = System.currentTimeMillis() + 4000L // The time the glass will stay for if the player walks away
                }
            }
        }
    }


}