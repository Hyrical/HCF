package org.hyrical.hcf.version

import org.bukkit.entity.Player

interface Version {

    fun getItemInHand(player: Player)
    fun setItemInHand(player: Player)


}