package org.hyrical.hcf.staff

import org.bukkit.entity.Player

/**
 * Class created on 7/8/2023

 * @author 98ping
 * @project HCF
 * @website https://solo.to/redis
 */
abstract class StaffModeHook {

    abstract fun isModModed(player: Player) : Boolean
    abstract fun isVanished(player: Player) : Boolean
}