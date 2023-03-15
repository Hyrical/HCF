package org.hyrical.hcf.timer.type.impl.playertimers

import org.bukkit.Bukkit
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.registry.annotations.Listener
import org.hyrical.hcf.timer.event.TimerExpireEvent
import org.hyrical.hcf.timer.type.PlayerTimer
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate


@Listener
object LogoutTimer : PlayerTimer(HCFPlugin.instance.config.getInt("TIMERS.LOGOUT.TIME"), "LOGOUT") {

    @EventHandler
    fun timerExpire(event: TimerExpireEvent){
        if (event.timer !is LogoutTimer) return

        val player = Bukkit.getPlayer(event.player) ?: return
        player.kickPlayer(translate(LangFile.getString("LOGOUT.KICK-MSG")!!))
        // TODO: Despawn combat logger.
    }

    @EventHandler
    fun move(event: PlayerMoveEvent){
        if (!timers.containsKey(event.player.uniqueId)) return

        val player = event.player

        val to = event.to ?: return
        val from = event.from

        if (from.blockX == to.blockX && from.blockZ == to.blockZ) return

        timers.remove(player.uniqueId)
        player.sendMessage(translate(LangFile.getString("LOGOUT.CANCELLED")!!))
    }

    @EventHandler
    fun playerQuit(event: PlayerQuitEvent){
        if (!timers.containsKey(event.player.uniqueId)) return
        timers.remove(event.player.uniqueId)
    }

}