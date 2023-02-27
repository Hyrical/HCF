package org.hyrical.hcf.lunarclient

import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule
import com.lunarclient.bukkitapi.`object`.LCWaypoint
import com.lunarclient.bukkitapi.serverrule.LunarClientAPIServerRule
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.hyrical.hcf.lunarclient.waypoint.impl.SpawnWaypoint
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.plugin.PluginUtils

object LunarClientHandler {

    fun load(){
        if (!PluginUtils.isPlugin("LunarClient-API")) return
    }

    fun setup(player: Player){
        val team = player.getProfile()!!.team!!

        fixCombat(player)

        SpawnWaypoint(Location(Bukkit.getWorld("world"), 0.0, 63.0, 0.0)).send(player)
    }

    private fun fixCombat(player: Player){
        if (!PluginUtils.isPlugin("LunarClient-API")) return

        LunarClientAPIServerRule.setRule(ServerRule.LEGACY_COMBAT, true)
        LunarClientAPIServerRule.sendServerRule(player)
    }

}