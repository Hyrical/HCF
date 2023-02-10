package org.hyrical.hcf.provider.nametag.extra

enum class NameVisibility(val nameDisplay: String) {
    ALWAYS("always"), HIDE_FOR_OWN_TEAM("hideForOwnTeam"), NEVER("never"), HIDE_FOR_OTHER_TEAMS("hideForOtherTeams")
}