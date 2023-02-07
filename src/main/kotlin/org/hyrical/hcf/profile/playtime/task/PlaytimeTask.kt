package org.hyrical.hcf.profile.playtime.task

import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.profile.playtime.PlaytimeHandler

class PlaytimeTask : BukkitRunnable() {
    override fun run() {
        PlaytimeHandler.playtimeMap.forEach { (identifier, time) ->
            val profile = ProfileService.getProfile(identifier) ?: return@forEach

            profile.playtime = PlaytimeHandler.calculatePlaytime(profile)
            PlaytimeHandler.playtimeMap[identifier] = System.currentTimeMillis()

            ProfileService.save(profile)
        }
    }
}