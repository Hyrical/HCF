package org.hyrical.hcf.timer.type

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.nametag.NametagHandler
import org.hyrical.hcf.timer.Timer
import org.hyrical.hcf.timer.event.TimerExpireEvent
import org.hyrical.hcf.utils.time.TimeUtils
import java.util.UUID
import java.util.concurrent.TimeUnit

open class PlayerTimer(
    val time: Int,
    val configPathTimer: String,
) : Timer() {

    val timers: MutableMap<UUID, Long> = mutableMapOf()

    override fun getTimerTime(): Long {
        return (TimeUnit.SECONDS.toMillis((time).toLong()))
    }

    override fun getConfigPath(): String {
        return "TIMERS.PLAYER.$configPathTimer"
    }

    fun applyTimer(player: Player) {
        timers[player.uniqueId] = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis((time).toLong())
    }

    fun applyTimer(player: Player, nametagUpdate: Boolean) {
        timers[player.uniqueId] = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis((time).toLong())

        if (nametagUpdate) HCFPlugin.instance.nametagHandler.update()
    }

    fun hasTimer(player: Player): Boolean {
        return timers[player.uniqueId] != null && timers[player.uniqueId]!! >= System.currentTimeMillis()
    }

    fun removeTimer(player: Player) {
        timers.remove(player.uniqueId)
    }

     fun getRemainingTime(player: Player): Long? {
        if (!timers.containsKey(player.uniqueId)) return null

        return timers[player.uniqueId]!! - System.currentTimeMillis()
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