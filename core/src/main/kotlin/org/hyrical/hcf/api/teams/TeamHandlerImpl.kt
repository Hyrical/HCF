package org.hyrical.hcf.api.teams

import org.hyrical.hcf.teams.Team
import org.hyrical.hcf.teams.TeamHandler
import java.util.*

class TeamHandlerImpl : TeamHandler() {

    override fun all(): List<Team> {
        return
    }

    override fun getTeamByName(name: String): Team? {

    }

    override fun getTeamByUUID(uuid: UUID): Team? {

    }

    override fun getTeamByUsername(name: String): Team? {

    }

    private fun mapOldToNew(team: org.hyrical.hcf.team.Team): Team {
        return Team(

        )
    }
}