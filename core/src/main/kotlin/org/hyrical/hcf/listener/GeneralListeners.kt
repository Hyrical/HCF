package org.hyrical.hcf.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.hyrical.hcf.utils.getProfile

@org.hyrical.hcf.registry.annotations.Listener
object GeneralListeners : Listener {

    @EventHandler
    fun join(event: PlayerJoinEvent){
        event.joinMessage = ""
        if (!event.player.hasPlayedBefore()){
            val player = event.player
            //val profile = player.getProfile()!!
        }
    }

    @EventHandler
    fun quit(event: PlayerQuitEvent){
        event.quitMessage = ""
    }
}