package org.hyrical.hcf

import co.aikar.commands.PaperCommandManager
import org.bukkit.plugin.java.JavaPlugin
import org.hyrical.hcf.storage.StorageService
import org.hyrical.store.repository.Repository

class HCFPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: HCFPlugin
    }

    val commandManager: PaperCommandManager = PaperCommandManager(this)

    override fun onEnable() {
        instance = this

        StorageService.start()
    }
}