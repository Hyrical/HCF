package org.hyrical.hcf.team.system

import org.hyrical.hcf.HCFPlugin

enum class Flag(val key: String) {
    CITADEL("CITADEL"),
    SPAWN("SPAWN"),
    SPAWN("SPAWN"),
    NO_PEARL("");

    fun getColor(enum: Flag): String {
        return if (enum.key == ""
            || HCFPlugin.instance.config.getString("TEAM-COLORS.$key") == null) ""
        else HCFPlugin.instance.config.getString("TEAM-COLORS.$key")!!
    }
}