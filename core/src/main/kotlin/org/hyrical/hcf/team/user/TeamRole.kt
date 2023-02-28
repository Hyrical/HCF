package org.hyrical.hcf.team.user

import org.hyrical.hcf.teams.HCFTeamRole

enum class TeamRole(val formattedName: String, val weight: Int) {

    LEADER("Leader", 4),
    COLEADER("Co-Leaders", 3),
    CAPTAIN("Captains", 2),
    MEMBER("Members", 1);

    fun toAPI(): HCFTeamRole {
        return when (this) {
            LEADER -> HCFTeamRole.LEADER
            COLEADER -> HCFTeamRole.COLEADER
            CAPTAIN -> HCFTeamRole.CAPTAIN
            MEMBER -> HCFTeamRole.MEMBER
        }
    }
}