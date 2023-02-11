package org.hyrical.hcf.timer.type

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.timer.Timer
import org.hyrical.hcf.timer.event.TimerExpireEvent
import java.util.UUID

open class PlayerTimer(
    val time: Long,
    val configPathTimer: String,
) : Timer() {

    val timers: MutableMap<UUID, Long> = mutableMapOf()

    override fun getTimerTime(): Long {
        return time
    }

    override fun getConfigPath(): String {
        return "TIMERS.PLAYER.$configPathTimer"
    }

    override fun applyTimer(player: Player) {
        timers[player.uniqueId] = System.currentTimeMillis() + time
    }

    override fun hasTimer(player: Player): Boolean {
        return timers[player.uniqueId] != null && timers[player.uniqueId]!! >= System.currentTimeMillis()
    }

    override fun removeTimer(player: Player) {
        timers.remove(player.uniqueId)
    }

    override fun getRemainingTime(player: Player): Long? {
        return timers[player.uniqueId]
    }

    override fun run() {
        for (entry in timers){
            if (entry.value - System.currentTimeMillis() < 0L){
                Bukkit.getPluginManager().callEvent(TimerExpireEvent(entry.key, this))
                timers.remove(entry.key)
                return
            }
        }
    }
}