package org.hyrical.hcf.chat

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.hyrical.hcf.chat.mode.ChatMode
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.utils.getProfile
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

            }
            ChatMode.OFFICER -> {
                if (!team.isCaptain(player.uniqueId)){
                    player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!.replace("%role%", "Captain")))
                    return
                }

                for (entry in team.members) {
                    if (!team.isCaptain(entry.uuid)) continue

                    Bukkit.getPlayer(entry.uuid)?.sendMessage(translate(LangFile.getString("")!!))
                }
            }

            else -> {

            }
        }

    }
}