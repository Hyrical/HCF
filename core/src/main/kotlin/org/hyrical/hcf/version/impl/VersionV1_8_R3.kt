package org.hyrical.hcf.version.impl

import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.tab.extra.TabSkin
import org.hyrical.hcf.version.Version

class VersionV1_8_R3 : Version {
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

        val packet = PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent.ChatSerializer.a("{\"text\":\"$header\"}"))
        val field = packet.javaClass.getDeclaredField("b")
        field.isAccessible = true
        field.set(packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"$footer\"}"))
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun addPlayerToSkins(player: Player) {
        val gameProfile = (player as CraftPlayer).profile

        HCFPlugin.instance.tabHandler.skins[player.uniqueId.toString()] = TabSkin(gameProfile.properties["textures"].first().signature, gameProfile.properties["textures"].first().value)
    }
}