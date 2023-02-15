package org.hyrical.hcf.timer.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hyrical.hcf.timer.Timer
import java.util.UUID

class TimerExpireEvent(val player: UUID, val timer: Timer) : Event() { // event

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        return getHandlerList()
    }
}