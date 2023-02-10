package org.hyrical.hcf.provider.nametag

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.provider.nametag.packet.NametagPacket
import org.hyrical.hcf.version.VersionManager
import java.util.concurrent.Executors


class NametagHandler(val adapter: NametagAdapter) {

    val executor = Executors.newSingleThreadExecutor()

    fun update() {
        for (player in Bukkit.getOnlinePlayers()){
            for (target in Bukkit.getOnlinePlayers()){
                executor.execute {
                    val update = adapter.getAndUpdate(player, target)
                }
            }
        }
    }

    fun createPacket(player: Player?): NametagPacket {
        val version = "org.hyrical.hcf.provider.nametag.packet.type.NametagPacketV" + VersionManager.getNMSVer()
        return Class.forName(version).getConstructor(NametagHandler::class.java, Player::class.java)
            .newInstance(this, player) as NametagPacket
    }

}