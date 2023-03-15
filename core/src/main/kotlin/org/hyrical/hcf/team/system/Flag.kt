package org.hyrical.hcf.team.system

import org.hyrical.hcf.HCFPlugin

enum class Flag(val key: String, val displayName: String) {
    CITADEL("CITADEL", "Citadel"),
    SPAWN("SPAWN", "Spawn"),
    KOTH("KOTH", "KoTH"),
    ROAD("ROAD", "Road"),
    GLOWSTONE_MOUNTAIN("GLOWSTONE", "Glowstone Mountain"),
    CONQUEST("CONQUEST", "Conquest"),
    NO_ENDERPEARL("", "&6No Enderpearl Zone");

    companion object {
        fun getColor(enum: Flag): String {
            return if (enum.key == ""
                || HCFPlugin.instance.config.getString("TEAM-COLORS.${enum.key}") == null) ""
            else HCFPlugin.instance.config.getString("TEAM-COLORS.${enum.key}")!!
        }
    }

}