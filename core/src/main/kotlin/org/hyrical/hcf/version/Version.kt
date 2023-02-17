package org.hyrical.hcf.version

import org.bukkit.entity.Player

interface Version {

    fun getItemInHand(player: Player)
    fun setItemInHand(player: Player)

    fun sendHeaderFooter(player: Player, header: String, footer: String)
    fun addPlayerToSkins(player: Player)

    fun sendBlockChange(player: Player)
}