package org.hyrical.hcf.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hyrical.hcf.utils.getProfile

@org.hyrical.hcf.registry.annotations.Listener
object JoinListener : Listener {

    @EventHandler
    fun join(event: PlayerJoinEvent){
        if (!event.player.hasPlayedBefore()){
            val player = event.player
            val profile = player.getProfile()!!


        }
    }
}