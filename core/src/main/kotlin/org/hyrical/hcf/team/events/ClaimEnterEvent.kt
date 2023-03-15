package org.hyrical.hcf.team.events

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.claim.cuboid.Cuboid

class ClaimEnterEvent(
    player: Player,
    val to: Pair<Team, Cuboid>?,
    val from: Pair<Team, Cuboid>?,
    var isCancelledYes: Boolean = false
) : PlayerEvent(player), Cancellable {
    override fun getHandlers(): HandlerList {
        return HandlerList()
    }

    override fun isCancelled(): Boolean {
        return isCancelledYes
    }

    override fun setCancelled(p0: Boolean) {
        isCancelledYes = p0
    }
}