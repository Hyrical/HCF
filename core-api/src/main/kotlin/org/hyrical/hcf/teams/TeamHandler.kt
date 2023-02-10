package org.hyrical.hcf.teams

import java.util.UUID

abstract class TeamHandler {
    open fun all(): List<HCFTeam> {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getTeamByName(name: String): HCFTeam? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getTeamByUUID(uuid: UUID): HCFTeam? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getTeamByUsername(name: String): HCFTeam? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }
}