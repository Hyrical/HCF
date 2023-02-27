package org.hyrical.hcf.walls

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.team.claim.ClaimHandler
import org.hyrical.hcf.utils.plugin.PluginUtils

class WallThread : Thread() {

    override fun run() {
        while (true) {
            try {
                for (player in PluginUtils.getOnlinePlayers()) {
                    tick(player)
                }

                sleep(250)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun tick(player: Player) {

        try {
            if (!player.world.isChunkLoaded(player.location.blockX shr 4, player.location.blockZ shr 4)) return

            WallHandler.clearWalls(player)

            val cuboids = ClaimHandler.getNearbyCuboids(player.location, 8)

            for (cuboid in cuboids){
                WallHandler.sendWall(player, cuboid)
            }

        } catch (ex: Exception){
            ex.printStackTrace()
        }
    }
}