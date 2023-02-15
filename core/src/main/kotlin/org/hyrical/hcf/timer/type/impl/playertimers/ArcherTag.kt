package org.hyrical.hcf.timer.type.impl.playertimers

import org.bukkit.Material
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.provider.nametag.NametagHandler
import org.hyrical.hcf.registry.annotations.Listener
import org.hyrical.hcf.timer.event.TimerExpireEvent
import org.hyrical.hcf.timer.type.PlayerTimer
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate

@Listener
object ArcherTag : PlayerTimer(HCFPlugin.instance.config.getInt("TIMERS.ARCHER-TAG.TIME"), "ARCHER-TAG"){

    @EventHandler
    fun timerExpire(event: TimerExpireEvent){
        if (event.timer !is ArcherTag) return

        HCFPlugin.instance.nametagHandler.update()
    }
}