package org.hyrical.hcf.sign

import org.bukkit.Bukkit
import org.hyrical.hcf.sign.impl.ElevatorSign

object FundamentalClickableSignCommunicator {

    val signs = listOf(ElevatorSign())

    fun getCustomSign(lines: List<String>): ClickableSign? {
        Bukkit.broadcastMessage(lines.joinToString(" "))
        Bukkit.broadcastMessage(signs[0].getLines().joinToString(" "))
        return signs.first { it.getLines().toSet() == lines.toSet() }
    }

}