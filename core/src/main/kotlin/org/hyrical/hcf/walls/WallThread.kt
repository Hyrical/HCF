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
            tick()
        }
    }

    private fun tick() {
        for (player in PluginUtils.getOnlinePlayers()) {
            if (!cachedLocations.containsKey(player.uniqueId)) {
                cachedLocations[player.uniqueId] = mutableMapOf()
            }
            val claims = LinkedList<Cuboid>()
            for ((team, cuboid) in getTeamsAndClaimsNearLocation(player.location)) {
                if (cuboid.contains(player.location)) continue

                println("I HIT THE CUBIOD WITH THE SHOE AND HIT THE BIG BLACK BOO")

                if (team.leader == null) {
                    if (hasFlag(team, Flag.SPAWN) && CombatTimer.hasTimer(player)) {
                        claims.add(cuboid)
                        println("whby u built so goofy my nig")
                        continue
                    } else if (hasFlag(team, Flag.KOTH) && false/* Check for pvp timer here */) {

                    }
                } else if (/* Check for pvp timer here */false) {
                    claims.add(cuboid)
                }
            }

            println(claims.size)

            if (claims.size == 0) {
                // clear the walls we went them already
                clearBlocks(player)
                println("I cleared ther shit annd hit the nig")
                continue
            }

            val bordersIterator: Iterator<Map.Entry<Location, Long>> = cachedLocations[player.uniqueId]!!.iterator()

            while (bordersIterator.hasNext()) {
                val (loc, value) = bordersIterator.next()
                if (System.currentTimeMillis() >= value) {
                    if (!loc.world!!.isChunkLoaded(loc.blockX shr 4, loc.blockZ shr 4)) continue
                    val block: Block = loc.block
                    player.sendBlockChange(loc, block.type, block.data);
                }
            }

            for (claim in claims) {
                sendClaimToPlayer(player, claim)
            }
        }
    }

    private fun getTeamsAndClaimsNearLocation(location: Location): List<Pair<Team, Cuboid>> {
        val result = mutableListOf<Pair<Team, Cuboid>>()

        for (team in TeamManager.cache.values) {
            for (cuboid in team.claims) {
                val cuboidLocation = Location(cuboid.world, cuboid.lowerX.toDouble(), 0.0, cuboid.lowerZ.toDouble())
                val locationWithoutY = Location(location.world, location.x, 0.0, location.z)
                val distanceSquared = cuboidLocation.distanceSquared(locationWithoutY)
                if (distanceSquared <= 64) { // squared value of 8 blocks
                    result.add(Pair(team, cuboid))
                }
            }
        }

        return result
    }



    private fun clearBlocks(player: Player) {
        if (!cachedLocations.containsKey(player.uniqueId)) return

        for (location in cachedLocations[player.uniqueId]!!.keys) {
            if (!location.world!!.isChunkLoaded(location.blockX shr 4, location.blockZ shr 4)) continue
            val block = location.block
            player.sendBlockChange(location, block.type, block.data)
        }

        cachedLocations.remove(player.uniqueId)
    }


    private fun hasFlag(team: Team, flag: Flag): Boolean {
        return team.factionType.contains(flag)
    }

    private fun sendClaimToPlayer(player: Player, claim: Cuboid) {
        // This gets us all the coordinates on the outside of the claim.
        for (coordinate in claim.getCoordinatesOnEdge()) {
            val onPlayerY = Location(player.world, coordinate.first.toDouble(), player.location.y, coordinate.second.toDouble())

            // Ignore an entire pillar if the block closest to the player is further than the max distance (none of the others will be close enough, either)
            if (onPlayerY.distanceSquared(player.location) > 64) {
                continue
            }

            for (i in -4..4) {
                val check = onPlayerY.clone().add(0.0, i.toDouble(), 0.0)

                if (check.world!!.isChunkLoaded(check.blockX shr 4, check.blockZ shr 4)
                    && check.block.type.isTransparent && check.distanceSquared(onPlayerY) < 64) {
                    player.sendBlockChange(check, XMaterial.RED_STAINED_GLASS.parseMaterial()!!, 14) // Red stained glass
                    cachedLocations[player.uniqueId]!![check] = System.currentTimeMillis() + 4000L // The time the glass will stay for if the player walks away
                }
            }
        }
    }

}