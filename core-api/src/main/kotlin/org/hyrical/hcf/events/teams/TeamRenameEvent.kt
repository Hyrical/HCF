package org.hyrical.hcf.events.teams

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hyrical.hcf.teams.Team

/**
 * Cancellable incase you want to filter specific names out
 */
class TeamRenameEvent(val team: Team, val oldName: String, val newName: String) : Event(), Cancellable {
    override fun getHandlers(): HandlerList {
        return  HandlerList()
    }

    private var cancelled = false

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(p0: Boolean) {
        cancelled = p0
    }
}