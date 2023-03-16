package org.hyrical.hcf.chat

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.chat.mode.ChatMode
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.rank.RankExtensionManager
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.plugin.PluginUtils
import org.hyrical.hcf.utils.translate

object ChatListener : Listener {

    @EventHandler
    fun chat(event: AsyncPlayerChatEvent){
        val player = event.player
        val profile = player.getProfile() ?: return

        if (profile.chatMode != ChatMode.PUBLIC && profile.teamString == null){
            player.sendMessage(translate(LangFile.getString("TEAM.SPEAK_IN_CHAT_WITHOUT_TEAM")!!))
            return
        }

        val team = profile.team

        event.isCancelled = true

        when (profile.chatMode){
            ChatMode.PUBLIC -> {
                chat(player, ChatMode.PUBLIC, team, ChatColor.stripColor(event.message))
            }
            ChatMode.OFFICER -> {
                if (team == null) return
                if (!team.isCaptain(player.uniqueId) || !team.isLeader(player.uniqueId) || !team.isLeader(player.uniqueId)){
                    player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!.replace("%role%", "Captain")))
                    return
                }

                chat(player, ChatMode.OFFICER, team, ChatColor.stripColor(event.message))
                return
            }

            ChatMode.LEADER -> {
                if (team == null) return
                if (!team.isLeader(player.uniqueId) || !team.isLeader(player.uniqueId)){
                    player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!.replace("%role%", "Leader")))
                    return
                }

                chat(player, ChatMode.LEADER, team, ChatColor.stripColor(event.message))
                return
            }

            else -> {
                chat(player, ChatMode.PUBLIC, team, ChatColor.stripColor(event.message))
                profile.chatMode = ChatMode.PUBLIC
            }
        }
    }

    private fun chat(player: Player, mode: ChatMode, team: Team?, message: String){
        if (mode == ChatMode.PUBLIC){
            for (plr in PluginUtils.getOnlinePlayers()){
                plr.sendMessage(translate(HCFPlugin.instance.config.getString("CHAT-FORMAT.FORMAT")!!
                    .replace("%player%", player.displayName).replace("%rank%", RankExtensionManager.rankExtension.getRankDisplay(player))
                    .replace("%team%", if (team == null) "" else
                        HCFPlugin.instance.config.getString("CHAT-FORMAT.FORMAT-TEAM")!!
                            .replace("%relationColor%", team.getRelationColor(plr).replace("%name%", team.name))
                            .replace("%name%", team.getFormattedTeamName(plr))
                    ).replace("%s", message)))
            }
            return
        }

        for (entry in team!!.members) {
            if (mode == ChatMode.OFFICER){
                if (!team.isCaptain(entry.uuid) || !team.isCoLeader(player.uniqueId) || !team.isLeader(player.uniqueId)) continue

                Bukkit.getPlayer(entry.uuid)?.sendMessage(translate(LangFile.getString("TEAM.TEAM-CHAT.OFFICER.FORMAT")!!).replace("%s", message))

                continue
            } else if (mode == ChatMode.LEADER){
                if (!team.isCoLeader(player.uniqueId) || !team.isLeader(player.uniqueId)) continue

                Bukkit.getPlayer(entry.uuid)?.sendMessage(translate(LangFile.getString("TEAM.TEAM-CHAT.LEADER.FORMAT")!!).replace("%s", message))
                continue
            } else if (mode == ChatMode.ALLY){
                Bukkit.getPlayer(entry.uuid)?.sendMessage(translate(LangFile.getString("TEAM.TEAM-CHAT.ALLY.FORMAT")!!.replace("%s", message)))
                continue
            } else {
                Bukkit.getPlayer(entry.uuid)?.sendMessage(translate(LangFile.getString("TEAM.TEAM-CHAT.TEAM.FORMAT")!!).replace("%s", message))
                return
            }
        }
    }
}