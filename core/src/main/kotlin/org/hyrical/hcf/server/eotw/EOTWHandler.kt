package org.hyrical.hcf.server.eotw

import com.cryptomorin.xseries.XSound
import org.bukkit.Bukkit
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.utils.translate

object EOTWHandler {

    fun startPreEOTW(){
        ServerHandler.preEotw = true

        for (player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, XSound.ENTITY_WITHER_SPAWN.parseSound()!!, 1f, 1f)
        }

        val msg = """
            &c███████
            &c█&4█████&c█ &4[Pre-EOTW]
            &c█&4█&c█████ &c&lEOTW is about to commence.
            &c█&4████&c██ &cPvP protection is disabled.
            &c█&4█&c█████ &cAll player have been un-deathbanned.
            &c█&4█████&c█ &cAll deaths are now permanent.
            &c███████
        """.trimIndent()

        msg.forEach { translate(it.toString()) }

        Bukkit.broadcastMessage(msg)
    }

    fun startEOTW(){
        ServerHandler.eotw = true

        for (team in TeamManager.getTeams()){
            team.setDTR(-0.99)
        }

        for (player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, XSound.ENTITY_WITHER_SPAWN.parseSound()!!, 1f, 1f)
        }

        val msg = """
            &c███████
            &c█&4█████&c█
            &c█&4█&c█████ &4[EOTW] 
            &c█&4████&c██ &c&lEOTW has commenced.
            &c█&4█&c█████ &cAll SafeZones are now deathban.
            &c█&4█████&c█
            &c███████
        """.trimIndent()

        msg.forEach { translate(it.toString()) }

        Bukkit.broadcastMessage(msg)
    }

}