package org.hyrical.hcf.teams

import java.util.*

class HCFTeam(
    val identifier: String,
    val name: String,
    val leader: HCFTeamUser,
    val members: MutableList<HCFTeamUser> = mutableListOf(),
    val allies: MutableList<String> = mutableListOf(),
    val dtr: Double = 1.1,
    val kothCaptures: Int = 0,
    val citadelCaptures: Int = 0,
    val kills: Int = 0,
    val deaths: Int = 0,
    val balance: Double = 0.0,
    val friendlyFire: Boolean = false,
    val announcement: String = "",
    val claimLocked: Boolean = false,
    val hq: String? = null,
    val isRegenerating: Boolean = false,
    val invitations: MutableList<UUID> = mutableListOf()
)