package org.hyrical.hcf

import org.hyrical.hcf.player.PlayerHandler
import org.hyrical.hcf.teams.TeamHandler
import org.hyrical.hcf.timers.TimerHandler

abstract class HCFCore {

    companion object {
        lateinit var instance: HCFCore
    }

    open fun getTeamHandler(): TeamHandler {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getPlayerHandler(): PlayerHandler {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getTimerHandler(): TimerHandler {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }
}