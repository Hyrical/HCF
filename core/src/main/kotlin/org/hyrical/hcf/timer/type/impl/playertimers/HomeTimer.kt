package org.hyrical.hcf.timer.type.impl.playertimers

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.registry.annotations.Listener
import org.hyrical.hcf.serialize.LocationSerializer
import org.hyrical.hcf.timer.event.TimerExpireEvent
import org.hyrical.hcf.timer.type.PlayerTimer
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate

@Listener
object HomeTimer : PlayerTimer(HCFPlugin.instance.config.getInt("TIMERS.HOME.TIME"), "HOME") {

    @EventHandler
    fun timerExpire(event: TimerExpireEvent){
        if (event.timer !is HomeTimer) return

        val player = Bukkit.getPlayer(event.player) ?: return
        val profile = player.getProfile() ?: return

        val team = profile.team ?: return
        val location = LocationSerializer.deserialize(team.hq!!)

        player.teleport(location)
        player.sendMessage(translate(LangFile.getString("HOME.WARPED")!!
            .replace("%team%", team.name)))
    }

    @EventHandler
    fun move(event: PlayerMoveEvent){
        if (!timers.containsKey(event.player.uniqueId)) return

        val player = event.player

        val to = event.to ?: return
        val from = event.from

        if (from.blockX == to.blockX && from.blockZ == to.blockZ) return

        timers.remove(player.uniqueId)
        player.sendMessage(translate(LangFile.getString("HOME.MOVED")!!))
    }


}