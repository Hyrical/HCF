package org.hyrical.hcf.server

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.team.TeamManager

object ServerHandler {

    private val config = HCFPlugin.instance.config

    var isHCF: Boolean = config.getBoolean("SERVER-MODES.HCF")
    var isUHCF: Boolean = config.getBoolean("SERVER-MODES.UHCF")
    var isSoup: Boolean = config.getBoolean("SERVER-MODES.SOUP")
    var isKitMap: Boolean = config.getBoolean("SERVER-MODES.KITMAP")

    var preEotw: Boolean = false
    var eotw: Boolean = false

    var overworldWarzone: Int = config.getInt("OVERWORLD.WARZONE")
    var overworldClaiming: Int = config.getInt("OVERWORLD.CLAIMING")
    var overworldBorder: Int = config.getInt("OVERWORLD.BORDER")

    var netherWarzone: Int = config.getInt("NETHER.WARZONE")
    var netherBorder = config.getInt("NETHER.BORDER")

    var maxFactionSize = config.getInt("MAX-FACTION-SIZE")

    fun isWarzone(location: Location): Boolean {
        return when (location.world!!.environment) {
            World.Environment.NORMAL -> {
                location.distanceSquared(Location(location.world, 0.0, 0.0, 0.0)) <= overworldWarzone * overworldWarzone
            }
            World.Environment.NETHER -> {
                location.distanceSquared(Location(location.world, 0.0, 0.0, 0.0)) <= netherWarzone * netherWarzone
            }
            World.Environment.THE_END -> {
                false
            }
            else -> false
        }
    }

    fun getSpawnLocation(): Location {
        return Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0)
    }

    fun isClaiming(location: Location): Boolean {
        return when (location.world!!.environment) {
            World.Environment.NORMAL -> {
                location.distanceSquared(Location(location.world, 0.0, 0.0, 0.0)) <= overworldClaiming * overworldClaiming
            }
            else -> false
        }
    }

    fun getTeamDisplayName(player: Player, location: Location): String {
        val teamAtLocation = TeamManager.getTeamAtLocation(location)

        if (isWarzone(location)) return HCFPlugin.instance.config.getString("TEAM-COLORS.WARZONE")!! + "Warzone"
        if (teamAtLocation == null) return HCFPlugin.instance.config.getString("TEAM-COLORS.WILDERNESS")!! + "Wilderness"

        return teamAtLocation.getFormattedTeamName(player)
    }
}