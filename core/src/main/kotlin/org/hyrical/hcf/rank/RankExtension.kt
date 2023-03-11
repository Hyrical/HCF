package org.hyrical.hcf.rank

import org.bukkit.entity.Player

interface RankExtension {

    fun getRankDisplay(player: Player): String
    fun getTagPrefix(player: Player): String?

}