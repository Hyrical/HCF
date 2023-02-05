package org.hyrical.hcf.profile

import org.bukkit.command.CommandSender
import org.hyrical.store.Storable

data class Profile(
    override val identifier: String,
    var name: String,
    var kills: Int = 0,
    var deaths: Int = 0,
    var killstreak: Int = 0,
    var highestKillstreak: Int = 0,
    var balance: Int = 0,
    var coalMined: Int = 0,
    var ironMined: Int = 0,
    var goldMined: Int = 0,
    var diamondMined: Int = 0,
    var emeraldMined: Int = 0,
    var lapisMined: Int = 0,
    var redstoneMined: Int = 0,
    var joinedAt: Long = System.currentTimeMillis(),
    var soulboundLives: Int = 0,
    var friendLives: Int = 0,
    var reclaimed: Boolean = false,
    var playtime: Long = 0,
    var chatMode: ChatMode = ChatMode.PUBLIC,
) : Storable {

    val lives: Int
        get() = soulboundLives + friendLives
}