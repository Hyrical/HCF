package org.hyrical.hcf.sign.impl

import org.bukkit.entity.Player
import org.hyrical.hcf.sign.ClickableSign

class ElevatorSign : ClickableSign {
    override fun getLines(): ArrayList<String> {
        return arrayListOf("&6[Elevator]", "&fUp")
    }

    override fun onClick(player: Player) {
        println("mf")
    }
}