package org.hyrical.hcf.profile.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.profile.playtime.PlaytimeHandler

@org.hyrical.hcf.registry.annotations.Listener
object PlaytimeListener : Listener {

    /**
     * Updates the playtime for a player when they quit the server.
     *
     * @param event the player quit event
     */
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val profile = HCFPlugin.instance.profileService.getProfile(event.player.uniqueId) ?: return

        profile.playtime = PlaytimeHandler.calculatePlaytime(profile)

        HCFPlugin.instance.profileService.save(profile)

        PlaytimeHandler.playtimeMap.remove(profile.identifier)
    }

    /**
     * Stores the current time as the start time for playtime calculation for a player when they join the server.
     *
     * @param event the player join event
     */
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val profile = HCFPlugin.instance.profileService.getProfile(event.player.uniqueId) ?: return

        PlaytimeHandler.playtimeMap[profile.identifier] = System.currentTimeMillis()
    }
}