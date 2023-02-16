package org.hyrical.hcf.provider.tab.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.tab.Tab
import org.hyrical.hcf.provider.tab.extra.TabSkin
import org.hyrical.hcf.version.VersionManager

@org.hyrical.hcf.registry.annotations.Listener
class TabListener : Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent){
        HCFPlugin.instance.tabHandler.tablists.remove(event.player.uniqueId)

        HCFPlugin.instance.tabHandler.skins.remove(event.player.uniqueId.toString())
    }

    @EventHandler
    fun onQuit(event: PlayerJoinEvent){
        val player = event.player
        val tab = Tab(player)

        object : BukkitRunnable(){
            override fun run() {
                HCFPlugin.instance.tabHandler.tablists[player.uniqueId] = tab
            }
        }.runTaskLater(HCFPlugin.instance, 10L)

        VersionManager.currentVersion.addPlayerToSkins(event.player)

        Bukkit.broadcastMessage(HCFPlugin.instance.tabHandler.skins.size.toString())
    }
}