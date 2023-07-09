package org.hyrical.hcf.staff.bukkit

import org.bukkit.entity.Player
import org.hyrical.hcf.staff.StaffModeHook

/**
 * Class created on 7/8/2023

 * @author 98ping
 * @project HCF
 * @website https://solo.to/redis
 */
class BukkitModModeHook : StaffModeHook() {
    override fun isModModed(player: Player): Boolean {
        return player.hasMetadata("modmode")
    }

    override fun isVanished(player: Player): Boolean {
        return player.hasMetadata("vanish")
    }
}