package org.hyrical.hcf.events.timer

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hyrical.hcf.timers.HCFTimer

class TimerExpireEvent(val timer: HCFTimer) : Event() {
    override fun getHandlers(): HandlerList {
        return HandlerList()
    }
}