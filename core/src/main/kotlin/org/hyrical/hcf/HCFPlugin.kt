package org.hyrical.hcf

import co.aikar.commands.PaperCommandManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.hyrical.hcf.api.HCFCoreImpl
import org.hyrical.hcf.classes.ArmorClassHandler
import org.hyrical.hcf.commands.TestCmd
import org.hyrical.hcf.config.impl.*
import org.hyrical.hcf.listener.DeathListener
import org.hyrical.hcf.listener.GeneralListeners
import org.hyrical.hcf.profile.listeners.ProfileCacheListener
import org.hyrical.hcf.profile.playtime.task.PlaytimeTask
import org.hyrical.hcf.provider.nametag.NametagHandler
import org.hyrical.hcf.provider.nametag.impl.HCFNametags
import org.hyrical.hcf.provider.nametag.listener.NametagListener
import org.hyrical.hcf.provider.scoreboard.ScoreboardHandler
import org.hyrical.hcf.provider.tab.TabManager
import org.hyrical.hcf.provider.tab.impl.HCFTab
import org.hyrical.hcf.storage.StorageService
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.commands.TeamCommand
import org.hyrical.hcf.team.param.TeamParamType
import java.util.concurrent.TimeUnit

class HCFPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: HCFPlugin
    }

    val commandManager: PaperCommandManager by lazy {
        PaperCommandManager(this)
    }

    lateinit var nametagHandler: NametagHandler
    lateinit var tabHandler: TabManager

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        StorageService.start()
        TeamManager.load()

        //RegistryService.enable()

        PlaytimeTask().runTaskTimerAsynchronously(this, 0L, TimeUnit.MINUTES.toSeconds(2L) * 20L)

        //Bukkit.getPluginManager().registerEvents(ProfileListener, this)
        Bukkit.getPluginManager().registerEvents(ProfileCacheListener, this)
        Bukkit.getPluginManager().registerEvents(DeathListener, this)
        Bukkit.getPluginManager().registerEvents(NametagListener, this)
        Bukkit.getPluginManager().registerEvents(GeneralListeners, this)

        commandManager.commandContexts.registerContext(Team::class.java, TeamParamType())
        commandManager.registerCommand(TeamCommand)
        commandManager.registerCommand(TestCmd)

        HCFCore.instance = HCFCoreImpl()

        DatabaseFile.loadConfig()
        LangFile.loadConfig()
        ScoreboardFile.loadConfig()
        TabFile.loadConfig()
        LunarFile.loadConfig()
        ClassFile.loadConfig()

        val hcfTab = HCFTab()
        hcfTab.load()

        ScoreboardHandler.load()

        nametagHandler = NametagHandler(HCFNametags())
        tabHandler = TabManager(hcfTab)

        ArmorClassHandler.load()
    }

    override fun onDisable() {

    }
}