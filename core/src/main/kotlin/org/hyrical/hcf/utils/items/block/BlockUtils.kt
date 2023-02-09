package org.hyrical.hcf.utils.items.block

import org.bukkit.Location
import org.bukkit.block.Block

object BlockUtils {
    fun getBlocksAroundCenter(loc: Location, radius: Int): List<Block> {
        val blocks: MutableList<Block> = ArrayList()
        for (x in loc.blockX - radius..loc.blockX + radius) {
            for (y in loc.blockY - radius..loc.blockY + radius) {
                for (z in loc.blockZ - radius..loc.blockZ + radius) {
                    val l = Location(loc.world, x.toDouble(), y.toDouble(), z.toDouble())
                    if (l.distance(loc) <= radius) {
                        blocks.add(l.block)
                    }
                }
            }
        }
        return blocks
    }
}
