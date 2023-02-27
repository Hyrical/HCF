package org.hyrical.hcf.lunarclient.waypoint

import com.lunarclient.bukkitapi.LunarClientAPI
import com.lunarclient.bukkitapi.`object`.LCWaypoint
import org.bukkit.Location
import org.bukkit.entity.Player
import org.hyrical.hcf.config.impl.LunarFile

open class Waypoint(
    val name: String,
    val location: Location,
    val key: String,
    val color: Int = LunarFile.getInt("WAYPOINTS.$key"),
) {

    val packet = LCWaypoint(name, location, color, true, true)

    fun send(player: Player){
        if (LunarClientAPI.getInstance().isRunningLunarClient(player)){
            LunarClientAPI.getInstance().sendWaypoint(player, packet)
        }
    }

    fun remove(player: Player){
        if (LunarClientAPI.getInstance().isRunningLunarClient(player)){
            LunarClientAPI.getInstance().removeWaypoint(player, packet)
        }
    }

}