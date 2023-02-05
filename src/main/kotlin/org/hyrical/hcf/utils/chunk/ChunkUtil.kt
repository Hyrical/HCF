package org.hyrical.hcf.utils.chunk

import org.bukkit.Chunk
import org.bukkit.Location

object ChunkUtil {
    fun isLoaded(location: Location): Boolean {
        return location.chunk.isLoaded
    }

    fun isLoaded(chunk: Chunk): Boolean {
        return chunk.isLoaded
    }
}