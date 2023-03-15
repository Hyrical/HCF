package org.hyrical.hcf.profile.playtime.task

import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.profile.playtime.PlaytimeHandler

class PlaytimeTask : BukkitRunnable() {
    override fun run() {
        PlaytimeHandler.playtimeMap.forEach { (identifier, _) ->
            val profile = HCFPlugin.instance.profileService.getProfile(identifier) ?: return@forEach

            profile.playtime = PlaytimeHandler.calculatePlaytime(profile)
            PlaytimeHandler.playtimeMap[identifier] = System.currentTimeMillis()

            HCFPlugin.instance.profileService.save(profile)
        }
    }
}