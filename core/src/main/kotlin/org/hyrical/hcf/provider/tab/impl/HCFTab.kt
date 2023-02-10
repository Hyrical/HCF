package org.hyrical.hcf.provider.tab.impl

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.TabFile
import org.hyrical.hcf.provider.tab.Tab
import org.hyrical.hcf.provider.tab.TabAdapter

class HCFTab : TabAdapter {
    private var farRightTablist: MutableList<String> = mutableListOf()
    private var leftTablist: MutableList<String> = mutableListOf()
    private var middleTablist: MutableList<String> = mutableListOf()
    private var rightTablist: MutableList<String> = mutableListOf()

    init {
        leftTablist = TabFile.getStringList("LEFT")
        middleTablist = TabFile.getStringList("MIDDLE")
        rightTablist = TabFile.getStringList("RIGHT");
        farRightTablist = TabFile.getStringList("FAR_RIGHT");
    }

    override fun getHeader(player: Player): Array<String> {
        val header: Array<String> = TabFile.getStringList("HEADER").toTypedArray()
        for (i in header.indices) {
            val text = header[i]
            header[i] = text.replace("%online%".toRegex(), java.lang.String.valueOf(Bukkit.getOnlinePlayers().size))
        }
        return header
    }

    override fun getInfo(player: Player): Tab {
        val tablist: Tab = HCFPlugin.instance.tabHandler.tablists[player.uniqueId]!!
        /*
        for (i in 0..19) {
            tablist.add(0, i, leftTablist[i])
            tablist.add(1, i, middleTablist[i])
            tablist.add(2, i, rightTablist[i])
            tablist.add(3, i, farRightTablist[i])
        }

         */

        tablist.add(0, 1, "LMFAOF")
        tablist.add(0, 1, "that's sick tbh")
        tablist.add(2, 2, "LMFAOFOAFo")

        return tablist
    }

    override fun getFooter(player: Player): Array<String> {
        val footer: Array<String> = TabFile.getStringList("FOOTER").toTypedArray()
        for (i in footer.indices) {
            val text = footer[i]
            footer[i] = text.replace("%online%".toRegex(), java.lang.String.valueOf(Bukkit.getOnlinePlayers().size))
        }
        return footer
    }

    fun load(){
        for (i in 0..19) {
            val left: List<String> = this.leftTablist[i].split(";");
            this.leftTablist[i] = if (left.size == 1) "" else left[1]
            val middle: List<String> = this.middleTablist[i].split(";")
            this.middleTablist[i] = if (middle.size == 1) "" else middle[1]
            val right: List<String> = this.rightTablist[i].split(";")
            this.rightTablist[i] = if (right.size == 1) "" else right[1]
            val farRight: List<String> = this.farRightTablist[i].split(";")
            this.farRightTablist[i] = if (farRight.size == 1) "" else farRight[1]
        }
    }
}