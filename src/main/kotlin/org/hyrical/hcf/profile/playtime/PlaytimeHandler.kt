package org.hyrical.hcf.profile.playtime

import org.hyrical.hcf.profile.Profile

object PlaytimeHandler {

    /**
     * A map of playtime data, keyed by the profile identifier.
     */
    val playtimeMap = mutableMapOf<String, Long>()

    /**
     * Calculates the total playtime for the specified [Profile] by adding the stored playtime to the elapsed time
     * since the last playtime update.
     *
     * @param profile the profile to calculate the playtime for
     * @return the total playtime for the profile
     */
    fun calculatePlaytime(profile: Profile): Long {
        val time = playtimeMap[profile.identifier] ?: return profile.playtime

        return profile.playtime + (System.currentTimeMillis() - time)
    }

}