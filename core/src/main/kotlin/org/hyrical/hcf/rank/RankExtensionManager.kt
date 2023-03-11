package org.hyrical.hcf.rank

import org.bukkit.entity.Player
import org.hyrical.hcf.rank.impl.AlchemistRankExtension
import org.hyrical.hcf.rank.impl.AquaCoreRankExtension
import org.hyrical.hcf.rank.impl.AtomRankExtension
import org.hyrical.hcf.rank.impl.MizuRankExtension
import org.hyrical.hcf.utils.plugin.PluginUtils

object RankExtensionManager {

    lateinit var rankExtension: RankExtension

    fun load(){
        if (q("Alchemist")) rankExtension = AlchemistRankExtension()
        if (q("AquaCore")) rankExtension = AquaCoreRankExtension()
        if (q("Atom")) rankExtension = AtomRankExtension()
        if (q("Mizu")) rankExtension = MizuRankExtension()
    }

    fun q(s: String): Boolean {
        return PluginUtils.isPlugin(s)
    }

}