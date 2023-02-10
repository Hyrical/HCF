package org.hyrical.hcf.provider.tab.thread

import org.bukkit.Bukkit
import org.hyrical.hcf.HCFPlugin

class TabThread : Thread() {

    override fun run() {
        while (true){
            try {
                for (player in Bukkit.getOnlinePlayers()){
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