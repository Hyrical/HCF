package org.hyrical.hcf.team.listeners

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.events.ClaimEnterEvent
import org.hyrical.hcf.utils.translate

object ClaimEnterListener : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun move(event: PlayerMoveEvent) {
        processInfo(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun teleport(event: PlayerTeleportEvent){
        processInfo(event)
    }

    private fun processInfo(event: PlayerMoveEvent){
        val player = event.player

        val from = event.from
        val to = event.to!!

        if (from.blockX == to.blockX && from.blockZ == to.blockZ) return

        val fromTeam = TeamManager.getTeamAndClaimAtLocation(from)
        val toTeam = TeamManager.getTeamAndClaimAtLocation(to)

        if (fromTeam == toTeam) return

        val claimEvent = ClaimEnterEvent(player, fromTeam, toTeam)

        Bukkit.getPluginManager().callEvent(claimEvent)

        if (claimEvent.isCancelled) return

        val configStrings = LangFile.getStringList("TEAM.CHANGE-CLAIM")

        val formattedFromTeam = ServerHandler.getTeamDisplayName(player, from)
        val formattedToTeam = ServerHandler.getTeamDisplayName(player, to)
        val replacedStrings = configStrings.map {
            translate(it.replace("%old_claim%", formattedFromTeam)
                .replace("%new_claim%", formattedToTeam)
                .replace("%old_deathban%", if (fromTeam?.first?.isSafeZone() == true) "&aNon-Deathban" else "&cDeathban" )
                .replace("%new_deathban%", if (fromTeam?.first?.isSafeZone() == true) "&aNon-Deathban" else "&cDeathban" ))
        }

        replacedStrings.forEach { player.sendMessage(it) }
    }

}