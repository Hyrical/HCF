package org.hyrical.hcf

import co.aikar.commands.PaperCommandManager
import org.bukkit.plugin.java.JavaPlugin
import org.hyrical.hcf.api.HCFCoreImpl
import org.hyrical.hcf.profile.playtime.task.PlaytimeTask
import org.hyrical.hcf.storage.StorageService
import org.hyrical.hcf.team.TeamManager
import java.util.concurrent.TimeUnit

class HCFPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: HCFPlugin
    }

    val commandManager: PaperCommandManager by lazy {
        PaperCommandManager(this)
    }

    override fun onEnable() {
        instance = this

        StorageService.start()

        TeamManager.load()

        PlaytimeTask().runTaskTimerAsynchronously(this, 0L, TimeUnit.MINUTES.toSeconds(2L) * 20L)

        HCFCore.instance = HCFCoreImpl()
    }

    override fun onDisable() {

    }
}