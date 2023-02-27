package org.hyrical.hcf.profile.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.profile.Profile
import org.hyrical.hcf.profile.ProfileService

@org.hyrical.hcf.registry.annotations.Listener
object ProfileCacheListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (HCFPlugin.instance.profileService.isCached(player.uniqueId)) return

        HCFPlugin.instance.profileService.getProfile(player.uniqueId).apply {
            val profile = this ?: HCFPlugin.instance.profileService.getProfile(player.uniqueId) ?: Profile(
                player.uniqueId.toString(),
                player.name
            ).apply {
                HCFPlugin.instance.profileService.save(this)
            }

            HCFPlugin.instance.profileService.cacheProfile(profile)
        }

    }
}