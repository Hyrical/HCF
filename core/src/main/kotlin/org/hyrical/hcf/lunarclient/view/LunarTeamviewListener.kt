package org.hyrical.hcf.lunarclient.view

import com.lunarclient.bukkitapi.LunarClientAPI
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTeammates
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import java.util.*


object LunarTeamviewListener : Listener {

    /*
       Runs update for teammates every time player moves from:
       A -> B, B -> A

       Running as MONITOR to fire last in case of error
       Does not run if player position is static to prevent yaw/pitch
       overload

       Time Complexity: O(n^2) [Can be improved upon later]
       Memory Complexity: O((items * size)^2)
     */
    @EventHandler(priority = EventPriority.MONITOR)
    fun runUpdate(event: PlayerMoveEvent) {
        val player = event.player
        val first = event.from
        val next = event.to ?: return
        val team = HCFPlugin.instance.profileService.getProfile(event.player.uniqueId)!!.team ?: return

        if (first.x != next.x || first.y != next.y || first.z != next.z)
        {
            LunarClientAPI.getInstance().sendPacket(player,
                LCPacketTeammates(player.uniqueId,
                    0L,
                    getOnlineTeammates(team)
                )
            )
        }
    }
    
    /*
       Searches for online teammates to let lunar know
       the markers we want on screen
    */
    private fun getOnlineTeammates(team: Team): Map<UUID, Map<String, Double>> {
        val players: MutableMap<UUID, Map<String, Double>> = hashMapOf()
        for (teammate in team.mapToBukkitPlayer()) {
            val coordinates: MutableMap<String, Double> = hashMapOf()

            coordinates["x"] = teammate.location.x
            coordinates["y"] = teammate.location.y
            coordinates["z"] = teammate.location.z
            players[teammate.uniqueId] = coordinates
        }

        return players
    }
}