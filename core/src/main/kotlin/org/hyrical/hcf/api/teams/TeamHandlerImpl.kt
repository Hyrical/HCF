package org.hyrical.hcf.api.teams

import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.teams.HCFTeam
import org.hyrical.hcf.teams.TeamHandler
import java.util.*

class TeamHandlerImpl : TeamHandler() {

    override fun all(): List<HCFTeam> {
        return TeamManager.getTeams().map { mapOldToNew(it) }
    }

    override fun getTeamByName(name: String): HCFTeam? {
        return TeamManager.getTeam(name)?.let { mapOldToNew(it) }
    }

    override fun getTeamByUUID(uuid: UUID): HCFTeam? {
        return HCFPlugin.instance.profileService.getProfile(uuid)?.team?.let { mapOldToNew(it) }
    }

    override fun getTeamByUsername(name: String): HCFTeam? {
        return HCFPlugin.instance.profileService.getProfile(name)?.let { it.teamString?.let { it1 -> TeamManager.getTeam(it1) } }?.let { mapOldToNew(it) }
    }

    private fun mapOldToNew(team: org.hyrical.hcf.team.Team): HCFTeam {
        return team.mapToAPI()
    }
}