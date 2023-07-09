package org.hyrical.hcf.staff

import org.hyrical.hcf.staff.bukkit.BukkitModModeHook

/**
 * Class created on 7/8/2023

 * @author 98ping
 * @project HCF
 * @website https://solo.to/redis
 */
object StaffModeManager {
    lateinit var activeHook: StaffModeHook

    fun enable() {
        //more to come using popular asf staff plugins
        //like super vanish
        this.activeHook = BukkitModModeHook()
    }
}