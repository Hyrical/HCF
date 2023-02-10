package org.hyrical.hcf.utils.sounds

import org.bukkit.Sound
import org.bukkit.entity.Player

enum class HCFSounds(
    val sound: Sound,
    val volume: Float,
    val pitch: Float
) {

    MENU_OPEN(Sound.UI_BUTTON_CLICK, 0.5f, 1.0f),
    CHEST_OPEN_1(Sound.BLOCK_CHEST_OPEN, 1.0f, 0.06349207f),
    DEATH(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.7936507f),
    ASSIST(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.7301587f),
    COUNTDOWN(Sound.BLOCK_NOTE_BLOCK_PLING, 20.0f, 1.0f);

    companion object {
        fun send(player: Player, enum: HCFSounds) {
            player.playSound(player.location, enum.sound, enum.volume, enum.pitch)
        }
    }
}