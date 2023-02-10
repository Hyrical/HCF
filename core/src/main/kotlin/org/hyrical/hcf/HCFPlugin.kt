package org.hyrical.hcf

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.hyrical.hcf.api.HCFCoreImpl
import org.hyrical.hcf.config.impl.DatabaseFile
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.config.impl.ScoreboardFile
import org.hyrical.hcf.config.impl.TabFile
import org.hyrical.hcf.listener.DeathListener
import org.hyrical.hcf.listener.JoinListener
import org.hyrical.hcf.profile.listeners.ProfileCacheListener
import org.hyrical.hcf.profile.listeners.ProfileListener
import org.hyrical.hcf.profile.playtime.task.PlaytimeTask
import org.hyrical.hcf.provider.nametag.NametagHandler
import org.hyrical.hcf.provider.nametag.impl.HCFNametags
import org.hyrical.hcf.registry.RegistryService
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
        //Bukkit.getPluginManager().registerEvents(JoinListener, this)

        commandManager.commandContexts.registerContext(Team::class.java, TeamParamType())
        commandManager.registerCommand(TeamCommand)

        HCFCore.instance = HCFCoreImpl()

        nametagHandler = NametagHandler(HCFNametags())

        DatabaseFile.loadConfig()
        LangFile.loadConfig()
        ScoreboardFile.loadConfig()
        TabFile.loadConfig()
    }

    override fun onDisable() {

    }
}