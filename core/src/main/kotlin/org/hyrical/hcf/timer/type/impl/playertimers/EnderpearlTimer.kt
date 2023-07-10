package org.hyrical.hcf.timer.type.impl.playertimers

import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.registry.annotations.Listener
import org.hyrical.hcf.timer.type.PlayerTimer
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate

@Listener
object EnderpearlTimer : PlayerTimer(HCFPlugin.instance.config.getInt("TIMERS.ENDERPEARL.TIME"), "ENDER-PEARL") {

    @EventHandler
    fun launch(event: ProjectileLaunchEvent) {
        if (event.entity !is EnderPearl) return
        if (event.entity.shooter !is Player) return

        val shooter = event.entity.shooter as Player

        if (!hasTimer(shooter)) {
            applyTimer(shooter)
        } else {
            event.isCancelled = true
            shooter.updateInventory()
            shooter.sendMessage(translate(LangFile.getString("TIMERS.ENDERPEARL-COOLDOWN")!!
                .replace("%seconds%", TimeUtils.formatIntoFancy(
                    getRemainingTime(shooter)!!
                ))))
        }
    }



}