package org.hyrical.hcf.utils

import org.bukkit.ChatColor

fun Any.getLang(key: String): String {
    return ""
}

fun Any.translate(s: String): String {
    return ChatColor.translateAlternateColorCodes('&', s)
}