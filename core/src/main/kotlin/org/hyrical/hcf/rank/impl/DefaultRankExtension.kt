package org.hyrical.hcf.rank.impl

import ltd.matrixstudios.alchemist.Alchemist
import ltd.matrixstudios.alchemist.api.AlchemistAPI
import ltd.matrixstudios.alchemist.service.profiles.ProfileGameService
import org.bukkit.entity.Player
import org.hyrical.hcf.rank.RankExtension

class DefaultRankExtension : RankExtension {
    override fun getRankDisplay(player: Player): String {
        return ""
    }

    override fun getTagPrefix(player: Player): String? {
        return ""
    }

}