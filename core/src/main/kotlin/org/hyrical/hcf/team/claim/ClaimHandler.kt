package org.hyrical.hcf.team.claim

import org.bukkit.Location
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.claim.cuboid.Cuboid


object ClaimHandler {

    fun getCurrentClaim(location: Location): Pair<Team, Cuboid>? {
        val team = TeamManager.getTeams().find { team ->
            team.claims.any { it.contains(location) }
        } ?: return null

        return team to team.claims.first { it.contains(location) }
    }


    fun getNearbyCuboids(location: Location, i: Int): List<Cuboid> {
        val claims: MutableList<Cuboid> = arrayListOf()

        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ
        for (f in -i..i) {
            for (t in -i..i) {

                val claim: Pair<Team, Cuboid>? = getCurrentClaim(Location(location.world, x + f.toDouble(), y.toDouble() + 2,z + t.toDouble()))
                if (claim != null) {
                    if (claims.contains(claim.second)) continue

                    claims.add(claim.second)
                }
            }
        }
        return claims
    }
}