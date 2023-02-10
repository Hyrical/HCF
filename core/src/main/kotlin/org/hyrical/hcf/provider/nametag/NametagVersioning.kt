package org.hyrical.hcf.provider.nametag

import org.hyrical.hcf.provider.nametag.packet.NametagPacket
import org.hyrical.hcf.provider.nametag.packet.type.NametagPacketV1_16_R3
import org.hyrical.hcf.provider.nametag.packet.type.NametagPacketV1_7_R4
import org.hyrical.hcf.provider.nametag.packet.type.NametagPacketV1_8_R3

object NametagVersioning {

    private val packets = mapOf(
        "1_7_R4" to NametagPacketV1_7_R4::class.java,
        "1_8_R3" to NametagPacketV1_8_R3::class.java,
        "1_16_R3" to NametagPacketV1_16_R3::class.java
    )

    fun versionToPacket(version: String): Class<NametagPacket> {
        return packets[version]!! as Class<NametagPacket>
    }
}