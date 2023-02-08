package org.hyrical.hcf.listener

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate

object DeathListener : Listener {

    @org.hyrical.hcf.registry.annotations.Listener
    fun death(event: PlayerDeathEvent){
        if (event.entity.killer !is Player) return

        val victim = event.entity
        val killer = event.entity.killer as Player

        val world = victim.world

        world.strikeLightningEffect(victim.location)

        val team = victim.getProfile()!!.team

        if (team != null){
            for (s in LangFile.getStringList("TEAM.MEMBER-LISTENER.MEMBER-DEATH")){
                team.sendTeamMessage(translate(s))
            }
        }

        handleDeath(victim, killer)
    }

    fun handleDeath(victim: Player, killer: Player?){

    }
}