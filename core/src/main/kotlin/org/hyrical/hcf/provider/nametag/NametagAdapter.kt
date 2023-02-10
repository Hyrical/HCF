package org.hyrical.hcf.provider.nametag

import org.bukkit.entity.Player

interface NametagAdapter {
    fun getAndUpdate(from: Player, to: Player): String
}