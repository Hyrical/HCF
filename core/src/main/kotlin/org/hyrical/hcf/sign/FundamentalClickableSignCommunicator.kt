package org.hyrical.hcf.sign

import org.bukkit.Bukkit
import org.hyrical.hcf.sign.impl.ElevatorSign

// TODO: Need to make sure that if it requires op and to make sure its coloured for signs like refill signs and so yeah
object FundamentalClickableSignCommunicator {

    private val signs = listOf(ElevatorSign())

    fun getCustomSign(lines: List<String>): ClickableSign? {
        return signs.firstOrNull { it -> it.getStripped().toSet() == lines.filter {  it.replace(" ", "").isNotEmpty() }.toSet() }
    }
}