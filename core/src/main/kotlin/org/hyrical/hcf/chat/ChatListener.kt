package org.hyrical.hcf.chat

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.hyrical.hcf.chat.mode.ChatMode
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.plugin.PluginUtils
import org.hyrical.hcf.utils.translate

object ChatListener : Listener {

    @org.hyrical.hcf.registry.annotations.Listener
    fun chat(event: AsyncPlayerChatEvent){
        val player = event.player
        val profile = player.getProfile() ?: return

        if (profile.chatMode != ChatMode.PUBLIC && profile.teamString == null){
            player.sendMessage(translate(LangFile.getString("TEAM.SPEAK_IN_CHAT_WITHOUT_TEAM")!!))
            return
        }

        val team = profile.team!!

        when (profile.chatMode){
            ChatMode.PUBLIC -> {
                chat(player, ChatMode.PUBLIC, team)
            }
            ChatMode.OFFICER -> {
                if (!team.isCaptain(player.uniqueId)){
                    player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!.replace("%role%", "Captain")))
                    return
                }

            }

            else -> {

            }
        }
    }

    fun chat(player: Player, mode: ChatMode, team: Team?){
        if (mode == ChatMode.PUBLIC){
            for (plr in PluginUtils.getOnlinePlayers()){
                player.sendMessage(translate(LangFile.getString("CHAT-FORMAT.FORMAT")!!
                    .replace("%player%", player.displayName).replace("%rank%", "temp ")
                    .replace("%team%", if (team == null) "" else
                        LangFile.getString("CHAT-FORMAT.FORMAT-TEAM")!!
                            .replace("%relationColor%", team.getRelationColor(plr).replace("%name%", team.name))
                    )))
            }
            return
        }

        for (entry in team!!.members) {
            if (!team.isCaptain(entry.uuid)) continue

            Bukkit.getPlayer(entry.uuid)?.sendMessage(translate(LangFile.getString("")!!))
        }
    }
}