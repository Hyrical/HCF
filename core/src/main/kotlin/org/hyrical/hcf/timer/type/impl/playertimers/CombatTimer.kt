package org.hyrical.hcf.timer.type.impl.playertimers

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.registry.annotations.Listener
import org.hyrical.hcf.timer.type.PlayerTimer

@Listener
object CombatTimer : PlayerTimer(HCFPlugin.instance.config.getInt("TIMERS.COMBAT.TIME") * 1000L, "SPAWN-TAG") {

    @EventHandler
    fun hit(event: EntityDamageByEntityEvent){
        if (event.entity !is Player || event.damager !is Player) return

        val victim = event.entity as Player
        val damager = event.damager as Player

        if (event.isCancelled) return

        if (!hasTimer(victim)){
            victim.sendMessage(LangFile.getString("TIMERS.TAGGED")!!.replace("%seconds%", HCFPlugin.instance.config.getInt("COMBAT.TIME").toString()))
        }

        if (!hasTimer(damager)){
            damager.sendMessage(LangFile.getString("TIMERS.TAGGED")!!.replace("%seconds%", HCFPlugin.instance.config.getInt("COMBAT.TIME").toString()))
        }

        applyTimer(victim)
        applyTimer(damager)
    }



}