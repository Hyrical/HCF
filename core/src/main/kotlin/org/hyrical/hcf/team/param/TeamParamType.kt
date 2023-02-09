package org.hyrical.hcf.team.param

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import org.bukkit.Bukkit
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate

class TeamParamType : ContextResolver<Team, BukkitCommandExecutionContext> {

    private val config = HCFPlugin.instance.config

    override fun getContext(c: BukkitCommandExecutionContext?): Team {
        val player = c!!.player
        val source = c.popFirstArg()

        if (player != null && source == null || source == "") {

            return player.getProfile()!!.team
                ?: throw InvalidCommandArgument(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))
        }

        val byName = TeamManager.getTeam(source)

        if (byName != null){
            return byName
        }

        val sourcePlayer = Bukkit.getPlayer(source)

        if (sourcePlayer != null){
            val bySourcePlayer = sourcePlayer.getProfile()

            if (bySourcePlayer!!.teamString != null){
                return bySourcePlayer.team!!
            }
        }

        val bukkitSource = Bukkit.getOfflinePlayer(source)

        if (bukkitSource.getProfile() != null){
            val byUUID = bukkitSource.getProfile()

            if (byUUID!!.teamString != null){
                return byUUID.team!!
            }
        }

        throw InvalidCommandArgument(LangFile.getString("TEAM.NO_TEAM_FOUND")!!.replace("%name%", source))
    }
}