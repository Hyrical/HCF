package org.hyrical.hcf.lunarclient.waypoint

import com.lunarclient.bukkitapi.LunarClientAPI
import com.lunarclient.bukkitapi.`object`.LCWaypoint
import org.bukkit.Location
import org.bukkit.entity.Player

open class Waypoint(
    val name: String,
    val location: Location,
    val color: Int,
) {

    fun send(player: Player){
        if (LunarClientAPI.getInstance().isRunningLunarClient(player)){
            val packet = LCWaypoint(name, location, color, true, true)

            LunarClientAPI.getInstance().sendWaypoint(player, packet)
        }
    }

    fun remove(player: Player){
        if (LunarClientAPI.getInstance().isRunningLunarClient(player)){
            val packet = LCWaypoint(name, location, color, true, true)

            LunarClientAPI.getInstance().removeWaypoint(player, packet)
        }
    }

}