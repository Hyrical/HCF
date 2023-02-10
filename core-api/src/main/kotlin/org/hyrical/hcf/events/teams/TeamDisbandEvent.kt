package org.hyrical.hcf.events.teams

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hyrical.hcf.teams.HCFTeam

class TeamDisbandEvent(val team: HCFTeam, val reason: Reason) : Event() {
    override fun getHandlers(): HandlerList {
        return HandlerList()
    }

    enum class Reason {
        CHOICE,
        EOTW,
        ADMIN
    }
}