package org.hyrical.hcf.player

import java.util.*

data class Profile(
    val uuid: String,
    val name: String,
    val kills: Int,
    val deaths: Int,
    val killstreak: Int,
    val highestKillstreak: Int,
    val balance: Int,
    val coalMined: Int,
    val ironMined: Int,
    val goldMined: Int,
    val diamondMined: Int,
    val emeraldMined: Int,
    val lapisMined: Int,
    val redstoneMined: Int,
    val joinedAt: Long,
    val soulboundLives: Int,
    val friendLives: Int,
    val reclaimed: Boolean,
    val playtime: Long
)