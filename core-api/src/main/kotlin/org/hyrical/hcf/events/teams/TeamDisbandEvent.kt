package org.hyrical.hcf.events.teams

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hyrical.hcf.teams.Team

class TeamDisbandEvent(val team: Team, val reason: Reason) : Event() {
    override fun getHandlers(): HandlerList {
        return HandlerList()
    }

    enum class Reason {
        CHOICE,
        EOTW,
        ADMIN
    }
}