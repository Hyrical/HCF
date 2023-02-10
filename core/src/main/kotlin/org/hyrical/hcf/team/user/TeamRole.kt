package org.hyrical.hcf.team.user

import org.hyrical.hcf.teams.HCFTeamRole

enum class TeamRole(val formattedName: String) {

    LEADER("Leader"),
    COLEADER("Co-Leaders"),
    CAPTAIN("Captains"),
    MEMBER("Members");

    fun toAPI(): HCFTeamRole {
        return when (this) {
            LEADER -> HCFTeamRole.LEADER
            COLEADER -> HCFTeamRole.COLEADER
            CAPTAIN -> HCFTeamRole.CAPTAIN
            MEMBER -> HCFTeamRole.MEMBER
        }
    }
}