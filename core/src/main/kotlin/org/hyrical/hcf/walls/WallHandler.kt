package org.hyrical.hcf.walls

import com.cryptomorin.xseries.XMaterial
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.hyrical.hcf.team.claim.cuboid.Cuboid
import java.util.*


object WallHandler {

    private val walls: HashMap<UUID, MutableList<Block>> = hashMapOf()

    fun load() {
        //WallThread().start()
    }

    fun sendBlockChange(player: Player, location: Location){
        player.sendBlockChange(location, XMaterial.RED_STAINED_GLASS.parseMaterial()!!, 14)
    }

    fun sendPillar(player: Player, location: Location){
        for (i in 0..256){
            sendBlockChange(player, location)
        }
    }

    fun sendWall(player: Player, claim: Cuboid) {
        if (!walls.containsKey(player.uniqueId)) {
            walls[player.uniqueId] = arrayListOf()
        }

        val walls = walls[player.uniqueId]!!
        for (block in claim.getWalls()) {
            if (block == null) continue
            if (block.location.distance(player.location) <= 7.0) {
                if (!block.chunk.isLoaded) {
                    continue
                }
                if (block.type.isSolid) {
                    continue
                }

                walls.remove(block)
                walls.add(block)
                sendBlockChange(player, block.location)
            }
        }
    }


    fun clearWalls(player: Player) {
        val walls: MutableList<Block> = this.walls[player.uniqueId] ?: return

        if (walls.isEmpty()) {
            return
        }

        val iterator: MutableIterator<Block> = walls.iterator()
        while (iterator.hasNext()) {
            val block: Block = iterator.next()
                if (block.location.distance(player.location) <= 7.0) continue

            player.sendBlockChange(block.location, block.type, block.data)

            iterator.remove()
        }
    }

}