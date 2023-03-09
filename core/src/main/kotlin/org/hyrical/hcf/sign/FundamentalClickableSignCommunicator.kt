package org.hyrical.hcf.sign

import org.hyrical.hcf.sign.impl.ElevatorSign

object FundamentalClickableSignCommunicator {

    val signs = listOf(ElevatorSign())

    fun getCustomSign(lines: List<String>): ClickableSign? {
        return signs.firstOrNull { it.getLines() == lines }
    }

}