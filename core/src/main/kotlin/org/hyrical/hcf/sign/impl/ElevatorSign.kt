package org.hyrical.hcf.sign.impl

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.sign.ClickableSign

class ElevatorSign : ClickableSign {
    override fun getLines(): ArrayList<String> {
        return arrayListOf("[Elevator]", "Up")
    }

    override fun onClick(event: PlayerInteractEvent) {
        Bukkit.broadcastMessage("shush vs elevator was clicked")
    }
}