package org.hyrical.hcf.provider.tab

import org.bukkit.entity.Player

interface TabAdapter {
    fun getHeader(player: Player): String
    fun getInfo(player: Player): Tab
    fun getFooter(player: Player): String
}