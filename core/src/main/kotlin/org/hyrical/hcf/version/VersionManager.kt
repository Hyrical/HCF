package org.hyrical.hcf.version

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager
import org.hyrical.hcf.version.impl.VersionV1_16_R3
import org.hyrical.hcf.version.impl.VersionV1_7_R4
import org.hyrical.hcf.version.impl.VersionV1_8_R3
import protocolsupport.api.ProtocolSupportAPI
import us.myles.ViaVersion.api.Via


object VersionManager {

    val versions = mapOf(
        "1_8_R3" to VersionV1_8_R3(),
        "1_16_R3" to VersionV1_16_R3(),
        "1_7_R4" to VersionV1_7_R4()
    )

    val currentVersion = versions[getNMSVer()]!!

    fun getNMSVer(): String? {
        val bukkit = Bukkit.getServer().javaClass.getPackage().name
        return bukkit.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[3].replace("v".toRegex(), "")
    }

    fun getProtocolVersion(player: Player): Int {
        val manager: PluginManager = Bukkit.getPluginManager()
        if (manager.getPlugin("ViaVersion") != null) {
            return Via.getAPI().getPlayerVersion(player.uniqueId)
        }
        return if (manager.getPlugin("ProtocolSupport") != null) {
            ProtocolSupportAPI.getProtocolVersion(player).id
        } else 100
    }
}