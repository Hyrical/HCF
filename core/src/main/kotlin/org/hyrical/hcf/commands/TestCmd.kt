package org.hyrical.hcf.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import com.comphenix.protocol.events.PacketContainer
import com.cryptomorin.xseries.particles.XParticle
import com.lunarclient.bukkitapi.LunarClientAPI
import com.lunarclient.bukkitapi.nethandler.client.LCPacketWorldBorder
import com.lunarclient.bukkitapi.nethandler.client.LCPacketWorldBorderCreateNew
import net.minecraft.server.v1_8_R3.PacketDataSerializer
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder
import net.minecraft.server.v1_8_R3.WorldBorder
import org.bukkit.entity.Player
import protocolsupport.protocol.packet.PacketType
import java.util.Random
import java.util.UUID
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.utils.items.ItemBuilder
import java.lang.Math.cos
import java.lang.Math.sin
import java.util.*

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

        /*
        val packet = LCPacketWorldBorderCreateNew(
            UUID.randomUUID().toString(),
            player.world.uid.toString(),
            true,
            false,
            false,
            0xFF0000,
            (player.location.blockX - 20).toDouble(),
            (player.location.blockZ - 20).toDouble(),
            (player.location.blockX + 20).toDouble(),
            (player.location.blockZ + 20).toDouble(),

        )

        LunarClientAPI.getInstance().sendPacket(player, packet)

         */
    }
}