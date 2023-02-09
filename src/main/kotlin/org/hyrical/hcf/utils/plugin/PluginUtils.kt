package org.hyrical.hcf.utils.plugin

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.hyrical.hcf.HCFPlugin

object PluginUtils {

    fun getPlugin(name: String): Plugin? {
        return Bukkit.getPluginManager().getPlugin(name)
    }

    fun isPlugin(name: String): Boolean {
        return getPlugin(name) != null
    }
}