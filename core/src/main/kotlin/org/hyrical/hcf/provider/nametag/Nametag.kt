package org.hyrical.hcf.provider.nametag

import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.nametag.packet.NametagPacket

class Nametag(
    val player: Player,
    val nametagPacket: NametagPacket = HCFPlugin.instance.nametagHandler.createPacket(player)
)