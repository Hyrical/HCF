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
}