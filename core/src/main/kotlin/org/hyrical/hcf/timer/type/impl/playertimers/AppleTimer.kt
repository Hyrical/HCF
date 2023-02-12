package org.hyrical.hcf.timer.type.impl.playertimers

import org.bukkit.Material
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.registry.annotations.Listener
import org.hyrical.hcf.timer.type.PlayerTimer
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate

@Listener
object AppleTimer : PlayerTimer(HCFPlugin.instance.config.getInt("TIMERS.GOLDEN-APPLE.TIME") * 1000L, "APPLE") {

    @EventHandler
    fun launch(event: PlayerItemConsumeEvent) {
        val player = event.player

        if (event.item.type != Material.GOLDEN_APPLE) return

        if (!hasTimer(player)) {
            applyTimer(player)
        } else {
            event.isCancelled = true
            player.sendMessage(translate(LangFile.getString("TIMERS.APPLE")!!
                .replace("%seconds%", TimeUtils.formatFancy(
                    getRemainingTime(player)!!
                ))))
        }
    }



}