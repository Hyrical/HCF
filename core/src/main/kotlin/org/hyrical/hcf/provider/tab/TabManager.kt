package org.hyrical.hcf.provider.tab

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.TabFile
import org.hyrical.hcf.provider.tab.extra.TabSkin
import org.hyrical.hcf.provider.tab.listener.TabListener
import org.hyrical.hcf.provider.tab.packet.TabPacket
import org.hyrical.hcf.provider.tab.thread.TabThread
import org.hyrical.hcf.version.VersionManager
import java.util.*

class TabManager(val adapter: TabAdapter) {
    val tablists: HashMap<UUID, Tab> = hashMapOf()
    val skins: HashMap<String, TabSkin> = hashMapOf()

    init {
        this.load()
        // start tablist thread
        TabThread().start()
        Bukkit.getPluginManager().registerEvents(TabListener(), HCFPlugin.instance)
    }

    private fun load(){
        for (s in TabFile.getConfigurationSection("SKINS")!!.getKeys(false)){
            val path = "SKINS.$s"
            this.skins[s] = TabSkin(TabFile.getString("$path.SIGNATURE")!!, TabFile.getString("$path.VALUE")!!)
        }
    }

    fun createPacket(player: Player): TabPacket {
        val skin = "org.hyrical.hcf.provider.tab.packet.type.TablistPacketV" + VersionManager.getNMSVer()
        return Class.forName(skin).getConstructor(Player::class.java)
            .newInstance(player) as TabPacket
    }

}