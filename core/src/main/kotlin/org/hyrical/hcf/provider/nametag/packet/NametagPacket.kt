package org.hyrical.hcf.provider.nametag.packet

import org.bukkit.entity.Player
import org.hyrical.hcf.provider.nametag.extra.NameVisibility


abstract class NametagPacket(val player: Player) {

    abstract fun addToTeam(player: Player?, team: String)

    abstract fun create(
        name: String,
        color: String,
        prefix: String,
        suffix: String,
        friendlyInvis: Boolean,
        visibilitt: NameVisibility
    )

}