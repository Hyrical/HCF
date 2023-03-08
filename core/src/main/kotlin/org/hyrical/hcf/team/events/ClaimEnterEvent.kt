package org.hyrical.hcf.team.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.claim.cuboid.Cuboid

class ClaimEnterEvent(
    player: Player,
    val to: Pair<Team, Cuboid>?,
    val from: Pair<Team, Cuboid>?
) : PlayerEvent(player) {
    override fun getHandlers(): HandlerList {
        return HandlerList()
    }
}