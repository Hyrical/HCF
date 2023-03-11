package org.hyrical.hcf.rank.impl

import ltd.matrixstudios.alchemist.Alchemist
import ltd.matrixstudios.alchemist.api.AlchemistAPI
import ltd.matrixstudios.alchemist.service.profiles.ProfileGameService
import me.activated.core.plugin.AquaCoreAPI
import org.bukkit.entity.Player
import org.hyrical.hcf.rank.RankExtension

class AquaCoreRankExtension : RankExtension {
    override fun getRankDisplay(player: Player): String {
        return AquaCoreAPI.INSTANCE.getPlayerRank(player.uniqueId).prefix
    }

    override fun getTagPrefix(player: Player): String? {
        return AquaCoreAPI.INSTANCE.getTagFormat(player)
    }

}