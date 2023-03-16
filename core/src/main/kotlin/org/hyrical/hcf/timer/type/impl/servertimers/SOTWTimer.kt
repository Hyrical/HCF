package org.hyrical.hcf.timer.type.impl.servertimers

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.hyrical.hcf.timer.Timer
import org.hyrical.hcf.utils.translate
import java.util.UUID

object SOTWTimer : Timer() {
    var timeRemaining: Long = 0
    val sotw: MutableList<UUID> = mutableListOf()

    fun start(time: Long) {
        timeRemaining = time
    }

    override fun getTimerTime(): Long {
        return timeRemaining
    }

    override fun getConfigPath(): String {
        return "TIMERS.PLAYERS.SOTW.ENABLED"
    }

    override fun run() {
    }

    fun isSOTWEnabled(player: Player): Boolean {
        return sotw.contains(player.uniqueId)
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun hit(event: EntityDamageByEntityEvent){
        if (event.damager !is Player || event.entity !is Player) return

        val damager = event.damager as Player
        val victim = event.entity as Player

        if (!isSOTWEnabled(victim) && (timeRemaining - System.currentTimeMillis()) > 0){
            event.isCancelled = true
        }
    }

    @EventHandler
    fun food(event: FoodLevelChangeEvent){
        if (event.entity !is Player) return

        if (!isSOTWEnabled(event.entity as Player) && (timeRemaining - System.currentTimeMillis()) > 0){
            event.isCancelled = true
        }
    }

    fun isSOTWActive(): Boolean {
        return (timeRemaining - System.currentTimeMillis()) > 0
    }
}