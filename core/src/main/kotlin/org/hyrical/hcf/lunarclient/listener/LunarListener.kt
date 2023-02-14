package org.hyrical.hcf.lunarclient.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hyrical.hcf.lunarclient.LunarClientHandler

object LunarListener : Listener {

    @EventHandler
    fun join(event: PlayerJoinEvent){
        val player = event.player

        LunarClientHandler.fixCombat(player)
    }

}