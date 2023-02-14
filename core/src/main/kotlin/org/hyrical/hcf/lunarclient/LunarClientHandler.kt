package org.hyrical.hcf.lunarclient

import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule
import com.lunarclient.bukkitapi.`object`.LCWaypoint
import com.lunarclient.bukkitapi.serverrule.LunarClientAPIServerRule
import org.bukkit.entity.Player
import org.hyrical.hcf.utils.plugin.PluginUtils

object LunarClientHandler {

    fun load(){
    }

    fun fixCombat(player: Player){
        if (!PluginUtils.isPlugin("LunarClient-API")) return

        LunarClientAPIServerRule.setRule(ServerRule.LEGACY_COMBAT, true)
        LunarClientAPIServerRule.sendServerRule(player)
    }

}