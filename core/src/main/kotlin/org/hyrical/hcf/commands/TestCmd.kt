package org.hyrical.hcf.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import com.comphenix.protocol.events.PacketContainer
import com.lunarclient.bukkitapi.LunarClientAPI
import com.lunarclient.bukkitapi.nethandler.client.LCPacketWorldBorderCreateNew
import net.minecraft.server.v1_8_R3.PacketDataSerializer
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder
import net.minecraft.server.v1_8_R3.WorldBorder
import org.bukkit.entity.Player
import protocolsupport.protocol.packet.PacketType
import java.util.Random

object TestCmd : BaseCommand() {

    @CommandAlias("test")
    fun test(player: Player) {
        /*
        val border = WorldBorder()
        border.setCenter(player.location.x + 20, player.location.z + 20)
        border.size = 5.0

        val packet = PacketPlayOutWorldBorder(border, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE)

        (player as org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer).handle.playerConnection.sendPacket(packet)

         */

        LunarClientAPI.getInstance().sendPacket(
            player,
            LCPacketWorldBorderCreateNew(
                "test-${kotlin.random.Random(5)}",
                player.world.name,
                true,
                false,
                false,
                0xFF0000,
                (player.location.blockX - 20).toDouble(),
                (player.location.blockZ - 20).toDouble(),
                (player.location.blockX + 20).toDouble(),
                (player.location.blockZ + 20).toDouble(),
                )
        )
    }
}