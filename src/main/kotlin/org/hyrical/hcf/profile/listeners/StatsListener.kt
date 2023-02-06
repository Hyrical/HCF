package org.hyrical.hcf.profile.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.hyrical.hcf.profile.ProfileService

@org.hyrical.hcf.registry.annotations.Listener
object StatsListener : Listener {

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val victimProfile = ProfileService.getProfile(event.entity.uniqueId)!!
        val killerProfile = event.entity.killer?.uniqueId?.let { ProfileService.getProfile(it) }

        victimProfile.deaths++
        victimProfile.killstreak = 0
        ProfileService.save(victimProfile)

        if (killerProfile != null) {
            killerProfile.kills++
            killerProfile.killstreak++
            killerProfile.highestKillstreak = killerProfile.highestKillstreak.coerceAtLeast(killerProfile.killstreak)
            ProfileService.save(killerProfile)
        }
    }
}