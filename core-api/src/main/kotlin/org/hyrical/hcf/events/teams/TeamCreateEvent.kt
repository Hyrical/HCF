package org.hyrical.hcf.events.teams

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hyrical.hcf.teams.Team

class TeamCreateEvent(val team: Team) : Event() {

    override fun getHandlers(): HandlerList {
        return HandlerList()
    }
}