package org.hyrical.hcf.api.teams

import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.teams.Team
import org.hyrical.hcf.teams.TeamHandler
import java.util.*

class TeamHandlerImpl : TeamHandler() {

    override fun all(): List<Team> {
        return TeamManager.getTeams().map { mapOldToNew(it) }
    }

    override fun getTeamByName(name: String): Team? {
        return TeamManager.getTeam(name)?.let { mapOldToNew(it) }
    }

    override fun getTeamByUUID(uuid: UUID): Team? {
        return ProfileService.getProfile(uuid)?.team?.let { mapOldToNew(it) }
    }

    override fun getTeamByUsername(name: String): Team? {
        return ProfileService.getProfile(name)?.let { it.teamString?.let { it1 -> TeamManager.getTeam(it1) } }?.let { mapOldToNew(it) }
    }

    private fun mapOldToNew(team: org.hyrical.hcf.team.Team): Team {
        return Team(

        )
    }
}