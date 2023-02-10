package org.hyrical.hcf.events.teams

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hyrical.hcf.teams.HCFTeam

class TeamCreateEvent(val team: HCFTeam) : Event() {

    override fun getHandlers(): HandlerList {
        return HandlerList()
    }
}