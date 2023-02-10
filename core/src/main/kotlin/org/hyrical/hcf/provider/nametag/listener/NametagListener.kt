package org.hyrical.hcf.provider.nametag.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.nametag.Nametag
import org.hyrical.hcf.provider.nametag.NametagHandler


@org.hyrical.hcf.registry.annotations.Listener
object NametagListener : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        HCFPlugin.instance.nametagHandler.nametags[player.uniqueId] = Nametag(player)
        HCFPlugin.instance.nametagHandler.update()
    }
}