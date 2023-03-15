package org.hyrical.hcf.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun expGive(event: BlockBreakEvent){
        event.player.giveExp(event.expToDrop)
        event.expToDrop = 0 // automatically gives them the xp if they get any
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun expGive(event: EntityDeathEvent){
        if (event.entity is Player) return

        val killer = event.entity.killer ?: return

        killer.giveExp(event.droppedExp)
        event.droppedExp = 0
    }


}