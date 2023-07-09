package org.hyrical.hcf

import co.aikar.commands.PaperCommandManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.hyrical.hcf.ability.AbilityService
import org.hyrical.hcf.ability.commands.AbilitiesCommand
import org.hyrical.hcf.ability.damage.DamageAbilityDispatcher
import org.hyrical.hcf.ability.interact.InteractAbilityDispatcher
import org.hyrical.hcf.api.HCFCoreImpl
import org.hyrical.hcf.chat.ChatListener
import org.hyrical.hcf.classes.ArmorClassHandler
import org.hyrical.hcf.commands.TagMeCommand
import org.hyrical.hcf.commands.TestCmd
import org.hyrical.hcf.config.impl.*
import org.hyrical.hcf.listener.*
import org.hyrical.hcf.lunarclient.LunarClientHandler
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
import org.hyrical.hcf.server.sotw.command.SOTWCommand
import org.hyrical.hcf.server.sotw.listener.SOTWListener
import org.hyrical.hcf.sign.FundamentalClickableSignListener
import org.hyrical.hcf.staff.StaffModeManager
import org.hyrical.hcf.storage.StorageService
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.claim.listener.ClaimListener
import org.hyrical.hcf.team.commands.TeamCommand
import org.hyrical.hcf.team.listeners.ClaimEnterListener
import org.hyrical.hcf.team.param.TeamParamType
import org.hyrical.hcf.timer.TimerHandler
import org.hyrical.hcf.walls.WallThread
import java.util.Random
import java.util.concurrent.TimeUnit

class HCFPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: HCFPlugin
        var RANDOM = Random()
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

        ///LicenceHandler.verify()

        StorageService.start()

        TeamManager.load()

        AbilityService.loadAll()

        StaffModeManager.enable()

        //RegistryService.enable()

        PlaytimeTask().runTaskTimerAsynchronously(this, 0L, TimeUnit.MINUTES.toSeconds(2L) * 20L)

        //Bukkit.getPluginManager().registerEvents(ProfileListener, this)
        Bukkit.getPluginManager().registerEvents(ProfileCacheListener, this)
        Bukkit.getPluginManager().registerEvents(DeathListener, this)
        Bukkit.getPluginManager().registerEvents(NametagListener, this)
        Bukkit.getPluginManager().registerEvents(GeneralListeners, this)
        Bukkit.getPluginManager().registerEvents(ChatListener, this)
        Bukkit.getPluginManager().registerEvents(InteractAbilityDispatcher, this)
        Bukkit.getPluginManager().registerEvents(DamageAbilityDispatcher, this)
        Bukkit.getPluginManager().registerEvents(FDListener, this)
        Bukkit.getPluginManager().registerEvents(ClaimListener, this)
        Bukkit.getPluginManager().registerEvents(ArmorDurabilityFixListener, this)
        Bukkit.getPluginManager().registerEvents(ClaimEnterListener, this)
        Bukkit.getPluginManager().registerEvents(SOTWListener, this)
        Bukkit.getPluginManager().registerEvents(FundamentalClickableSignListener, this)


        commandManager.commandContexts.registerContext(Team::class.java, TeamParamType())
        commandManager.registerCommand(TeamCommand)
        commandManager.registerCommand(TestCmd)
        commandManager.registerCommand(TagMeCommand)
        commandManager.registerCommand(AbilitiesCommand)
        commandManager.registerCommand(SOTWCommand)

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

        ScoreboardHandler.load()

        nametagHandler = NametagHandler(HCFNametags())
        tabHandler = TabManager(HCFTab())

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