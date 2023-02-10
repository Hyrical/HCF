package org.hyrical.hcf.provider.tab

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.tab.extra.TabEntry
import org.hyrical.hcf.provider.tab.packet.TabPacket

class Tab(val player: Player) {
    val entries: Table<Int, Int, TabEntry> = HashBasedTable.create()
    val tabPacket: TabPacket = HCFPlugin.instance.tabHandler.createPacket(player)
    val EMPTY_ENTRY: TabEntry = TabEntry("", -1)

    fun update() {
        entries.clear()
        tabPacket.update()
    }


    fun getEntries(x: Int, y: Int): TabEntry {
        val entry: TabEntry? = entries.get(x, y)

        if (entry == null) {
            entries.put(x, y, this.EMPTY_ENTRY)

            return this.EMPTY_ENTRY
        }
        return entry
    }


    fun add(x: Int, y: Int, name: String) {
        entries.put(x, y, TabEntry(name, -1))
    }

    fun getPacket(): TabPacket {
        return tabPacket
    }

    fun add(x: Int, y: Int, name: String, ping: Int) {
        entries.put(x, y, TabEntry(name, ping))
    }
}