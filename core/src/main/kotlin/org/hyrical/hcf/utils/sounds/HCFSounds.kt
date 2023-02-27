package org.hyrical.hcf.utils.sounds

import com.cryptomorin.xseries.XSound
import org.bukkit.Sound
import org.bukkit.entity.Player

enum class HCFSounds(
    val sound: Sound,
    val volume: Float,
    val pitch: Float
) {

    MENU_OPEN(XSound.UI_BUTTON_CLICK.parseSound()!!, 0.5f, 1.0f),
    CHEST_OPEN_1(XSound.BLOCK_CHEST_OPEN.parseSound()!!, 1.0f, 0.06349207f),
    DEATH(XSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound()!!, 1.0f, 1.7936507f),
    ASSIST(XSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound()!!, 1.0f, 1.7301587f),
    COUNTDOWN(XSound.BLOCK_NOTE_BLOCK_PLING.parseSound()!!, 20.0f, 1.0f);

    companion object {
        fun send(player: Player, enum: HCFSounds) {
            player.playSound(player.location, enum.sound, enum.volume, enum.pitch)
        }
    }
}