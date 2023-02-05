package org.hyrical.hcf

import org.bukkit.plugin.java.JavaPlugin

class HCFPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: HCFPlugin
    }

    override fun onEnable() {
        instance = this
    }
}