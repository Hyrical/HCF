package org.hyrical.hcf.provider.scoreboard.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.hyrical.hcf.provider.scoreboard.ScoreboardHandler

@org.hyrical.hcf.registry.annotations.Listener
object ScoreboardListener : Listener {

    @EventHandler
    fun quit(event: PlayerQuitEvent){
        ScoreboardHandler.delete(event.player)
    }

    @EventHandler
    fun join(event: PlayerJoinEvent){
        ScoreboardHandler.create(event.player)
    }
}