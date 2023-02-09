package org.hyrical.hcf.api

import org.hyrical.hcf.HCFCore
import org.hyrical.hcf.api.player.PlayerHandlerImpl
import org.hyrical.hcf.api.teams.TeamHandlerImpl
import org.hyrical.hcf.player.PlayerHandler
import org.hyrical.hcf.teams.TeamHandler
import org.hyrical.hcf.timers.TimerHandler

class HCFCoreImpl : HCFCore() {

    private val teamHandler = TeamHandlerImpl()
    private val playerHandler = PlayerHandlerImpl()

    override fun getTeamHandler(): TeamHandler {
        return teamHandler
    }

    override fun getPlayerHandler(): PlayerHandler {
        return playerHandler
    }

    override fun getTimerHandler(): TimerHandler {
        return super.getTimerHandler()
    }
}