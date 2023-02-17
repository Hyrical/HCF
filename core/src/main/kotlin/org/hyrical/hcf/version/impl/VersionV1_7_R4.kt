package org.hyrical.hcf.version.impl

import net.minecraft.server.v1_7_R4.ChatSerializer
import net.minecraft.server.v1_7_R4.IChatBaseComponent
import net.minecraft.util.org.apache.commons.lang3.StringEscapeUtils
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.tab.extra.TabSkin
import org.hyrical.hcf.version.Version
import org.spigotmc.ProtocolInjector
import org.spigotmc.ProtocolInjector.PacketTabHeader

class VersionV1_7_R4 : Version {

    override fun getItemInHand(player: Player) {
        TODO("Not yet implemented")
    }

    override fun setItemInHand(player: Player) {
        TODO("Not yet implemented")
    }

    override fun sendHeaderFooter(player: Player, header: String, footer: String) {
        header.replace("[", "")
        header.replace("]", "")
        footer.replace("[", "")
        footer.replace("]", "")
        val packet = PacketTabHeader(
            ChatSerializer.a("{\"text\":\"${StringEscapeUtils.escapeJava(header)}\"}"),
            ChatSerializer.a("{\"text\":\"${StringEscapeUtils.escapeJava(footer)}\"}")
        )

        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun sendBlockChange(player: Player){

    }

    override fun addPlayerToSkins(player: Player) {
        val gameProfile = (player as CraftPlayer).profile

        HCFPlugin.instance.tabHandler.skins[player.uniqueId.toString()] = TabSkin(gameProfile.properties["textures"].first().signature, gameProfile.properties["textures"].first().value)
    }
}