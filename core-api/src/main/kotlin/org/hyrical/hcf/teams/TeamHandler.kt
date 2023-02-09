package org.hyrical.hcf.teams

import java.util.UUID

abstract class TeamHandler {
    open fun all(): List<Team> {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getTeamByName(name: String): Team? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getTeamByUUID(uuid: UUID): Team? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getTeamByUsername(name: String): Team? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }
}