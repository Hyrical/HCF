package org.hyrical.hcf.team.listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.events.ClaimEnterEvent
import org.hyrical.hcf.utils.translate

object ClaimEnterListener : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun move(event: PlayerMoveEvent) {
        val player = event.player

        val from = event.from
        val to = event.to!!

        if (from.blockX == to.blockX && from.blockZ == to.blockZ) return

        val fromTeam = TeamManager.getTeamAndClaimAtLocation(from)
        val toTeam = TeamManager.getTeamAndClaimAtLocation(to)

        if (fromTeam?.first == toTeam?.first) return

        Bukkit.getPluginManager().callEvent(
            ClaimEnterEvent(player, fromTeam, toTeam)
        )

        val configStrings = LangFile.getStringList("TEAM.CHANGE-CLAIM").map { translate(it) }
        configStrings.map { toTeam?.first?.getFormattedTeamName(player)?.let { it1 -> it.replace("%new_claim%", it1) } }
        configStrings.map { fromTeam?.first?.getFormattedTeamName(player)?.let { it1 -> it.replace("%old_claim%", it1) } }

        configStrings.forEach {
            player.sendMessage(it)
        }
    }
}