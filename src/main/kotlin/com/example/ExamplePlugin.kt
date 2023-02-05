package com.example

import org.bukkit.plugin.java.JavaPlugin

class ExamplePlugin : JavaPlugin() {

    companion object{
        lateinit var instance: ExamplePlugin
    }

    override fun onEnable() {
        instance = this
    }

    override fun onDisable() {

    }
}