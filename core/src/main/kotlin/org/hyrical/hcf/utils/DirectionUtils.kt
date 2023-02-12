package org.hyrical.hcf.utils

import org.bukkit.entity.Player

object DirectionUtils {


    fun getCardinalDirection(player: Player): String? {
        var rot: Double = ((player.location.yaw - 90) % 360).toDouble()
        if (rot < 0) {
            rot += 360.0
        }
        return getDirection(rot)
    }

    /**
     * Converts a rotation to a cardinal direction name.
     *
     * @param rot
     * @return
     */
    fun getDirection(rot: Double): String? {
        return if (0 <= rot && rot < 22.5) {
            "W"
        } else if (22.5 <= rot && rot < 67.5) {
            "NW"
        } else if (67.5 <= rot && rot < 112.5) {
            "N"
        } else if (112.5 <= rot && rot < 157.5) {
            "NE"
        } else if (157.5 <= rot && rot < 202.5) {
            "E"
        } else if (202.5 <= rot && rot < 247.5) {
            "SE"
        } else if (247.5 <= rot && rot < 292.5) {
            "S"
        } else if (292.5 <= rot && rot < 337.5) {
            "SW"
        } else if (337.5 <= rot && rot < 360.0) {
            "W"
        } else {
            null
        }
    }
}