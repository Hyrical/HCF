package org.hyrical.hcf.rank

import org.bukkit.entity.Player
import org.hyrical.hcf.rank.impl.*
import org.hyrical.hcf.utils.plugin.PluginUtils

object RankExtensionManager {

    private var rankExtensionS: RankExtension? = null
    val rankExtension: RankExtension get(){
        return rankExtensionS!!
    }

    fun load(){
        if (q("Alchemist")) rankExtensionS = AlchemistRankExtension()
        if (q("AquaCore")) rankExtensionS = AquaCoreRankExtension()
        if (q("Atom")) rankExtensionS = AtomRankExtension()
        if (q("Mizu")) rankExtensionS = MizuRankExtension()

        if (rankExtensionS == null){
            rankExtensionS = DefaultRankExtension()
        }
    }

    fun q(s: String): Boolean {
        return PluginUtils.isPlugin(s)
    }

}