package org.hyrical.hcf.utils

import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.hyrical.hcf.profile.Profile
import org.hyrical.hcf.profile.ProfileService

fun Any.translate(s: String): String {
    return ChatColor.translateAlternateColorCodes('&', s)
}

fun Player.getProfile(): Profile? {
    return ProfileService.getProfile(this.uniqueId)
}

fun OfflinePlayer.getProfile(): Profile? {
    return ProfileService.getProfile(this.uniqueId)
}