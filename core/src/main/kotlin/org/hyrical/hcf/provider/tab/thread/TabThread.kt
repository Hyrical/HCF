package org.hyrical.hcf.provider.tab.thread

import org.bukkit.Bukkit
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.utils.plugin.PluginUtils

class TabThread : Thread() {

    override fun run() {
        while (true){
            try {
                for (player in PluginUtils.getOnlinePlayers()){
                    if (!HCFPlugin.instance.tabHandler.skins.containsKey(player.uniqueId.toString())) continue
                    val tab = HCFPlugin.instance.tabHandler.tablists[player.uniqueId]

                    tab?.update()
                }
            } catch (ex: Exception){
                ex.printStackTrace()
            }
            try {
                sleep(200L)
            } catch (ex: InterruptedException){
                ex.printStackTrace()
            }
        }
    }

}