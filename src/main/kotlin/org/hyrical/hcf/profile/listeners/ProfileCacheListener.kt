package org.hyrical.hcf.profile.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hyrical.hcf.profile.Profile
import org.hyrical.hcf.profile.ProfileService

@org.hyrical.hcf.registry.annotations.Listener
object ProfileCacheListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (ProfileService.isCached(player.uniqueId)) return

        ProfileService.getProfile(player.uniqueId).apply {
            val profile = this ?: ProfileService.getProfile(player.uniqueId) ?: Profile(
                player.uniqueId.toString(),
                player.name
            ).apply {
                ProfileService.save(this)
            }

            ProfileService.cacheProfile(profile)
        }

    }
}