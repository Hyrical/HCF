package org.hyrical.hcf

import co.aikar.commands.PaperCommandManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.hyrical.hcf.api.HCFCoreImpl
import org.hyrical.hcf.chat.ChatListener
import org.hyrical.hcf.classes.ArmorClassHandler
import org.hyrical.hcf.commands.TagMeCommand
import org.hyrical.hcf.commands.TestCmd
import org.hyrical.hcf.config.impl.*
import org.hyrical.hcf.listener.DeathListener
import org.hyrical.hcf.listener.GeneralListeners
import org.hyrical.hcf.lunarclient.LunarClientHandler
import org.hyrical.hcf.lunarclient.view.LunarTeamviewListener
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.profile.impl.JSONProfileService
import org.hyrical.hcf.profile.impl.MongoDBProfileService
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
import org.hyrical.hcf.timer.TimerHandler
import org.hyrical.hcf.walls.WallThread
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
    lateinit var profileService: ProfileService

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        // TODO: Embry if ur reading this, if you are testing plugin just comment this line out until the rest api has hosting and is done
        ///LicenceHandler.verify()

        StorageService.start()

        TeamManager.load()

        //RegistryService.enable()

        PlaytimeTask().runTaskTimerAsynchronously(this, 0L, TimeUnit.MINUTES.toSeconds(2L) * 20L)

        //Bukkit.getPluginManager().registerEvents(ProfileListener, this)
        Bukkit.getPluginManager().registerEvents(ProfileCacheListener, this)
        Bukkit.getPluginManager().registerEvents(DeathListener, this)
        Bukkit.getPluginManager().registerEvents(NametagListener, this)
        Bukkit.getPluginManager().registerEvents(GeneralListeners, this)
        Bukkit.getPluginManager().registerEvents(ChatListener, this)

        commandManager.commandContexts.registerContext(Team::class.java, TeamParamType())
        commandManager.registerCommand(TeamCommand)
        commandManager.registerCommand(TestCmd)
        commandManager.registerCommand(TagMeCommand)

        LunarClientHandler.load()

        HCFCore.instance = HCFCoreImpl()

        DatabaseFile.loadConfig()
        LangFile.loadConfig()
        ScoreboardFile.loadConfig()
        TabFile.loadConfig()
        LunarFile.loadConfig()
        ClassFile.loadConfig()
        StorageFile.loadConfig()

        TimerHandler.load()

        WallThread().start()

        val hcfTab = HCFTab()
        hcfTab.load()

        ScoreboardHandler.load()

        nametagHandler = NametagHandler(HCFNametags())
        tabHandler = TabManager(hcfTab)

        ArmorClassHandler.load()

        profileService = if (StorageFile.getString("PROFILES") == "MONGO") {
            MongoDBProfileService()
        } else {
            JSONProfileService()
        }
    }

    override fun onDisable() {

    }
}