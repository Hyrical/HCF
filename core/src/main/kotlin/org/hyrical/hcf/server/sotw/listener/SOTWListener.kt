package org.hyrical.hcf.server.sotw.listener

import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.timer.type.impl.servertimers.SOTWTimer
import org.hyrical.hcf.utils.translate

object SOTWListener : Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    fun entityDamage(event: EntityDamageEvent){
        if (event.entity !is Player) return
        if (!SOTWTimer.isSOTWActive()) return

        val player = event.entity as Player

        if (!SOTWTimer.isSOTWEnabled(player)) event.isCancelled = true

        if (event is EntityDamageByEntityEvent){

            var damager: Player? = null

            if (event.damager is Player) {
                damager = event.damager as Player
            } else if (event.damager is Projectile && (event.damager as Projectile).shooter is Player) {
                damager = (event.damager as Projectile).shooter as Player
            }

            if (damager != null && SOTWTimer.isSOTWEnabled(damager)){
                event.isCancelled = true
                for (line in LangFile.getStringList("SOTW.DAMAGE")){
                    player.sendMessage(translate(line))
                }
            }
        }

        if (event.cause != EntityDamageEvent.DamageCause.VOID) return
        if (SOTWTimer.isSOTWActive() && !SOTWTimer.isSOTWEnabled(player)) return

        event.entity.teleport(ServerHandler.getSpawnLocation())
    }
}