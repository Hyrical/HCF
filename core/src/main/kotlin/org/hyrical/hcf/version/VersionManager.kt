package org.hyrical.hcf.version

import org.bukkit.Bukkit




object VersionManager {

    fun getNMSVer(): String? {
        val bukkit = Bukkit.getServer().javaClass.getPackage().name
        return bukkit.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[3].replace("v".toRegex(), "")
    }
}