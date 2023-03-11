package org.hyrical.hcf.ability

import com.cryptomorin.xseries.XMaterial
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.ability.damage.DamageAbility
import org.hyrical.hcf.ability.interact.InteractAbility
import org.hyrical.hcf.ability.interact.impl.PortableBardAbility
import java.util.UUID

object AbilityService
{
    val interactAbilities = mutableMapOf<String, InteractAbility>()
    val damageAbilities = mutableMapOf<String, DamageAbility>()
    val cooldowns = mutableMapOf<String, Map<UUID, Long>>()

    fun loadAll() {
        interactAbilities["portable_strength"] = PortableBardAbility("portable_strength", "&c&lStrength II", "&7Right click to receive strength 2 for 3 seconds.", XMaterial.BLAZE_POWDER, PotionEffectType.INCREASE_DAMAGE, 3, 3)
        interactAbilities["portable_resistance"] = PortableBardAbility("portable_resistance", "&7&lResistance III", "&7Right click to receive resistance 3 for 3 seconds.", XMaterial.IRON_INGOT, PotionEffectType.DAMAGE_RESISTANCE, 4, 3)
        interactAbilities["portable_speed"] = PortableBardAbility("portable_speed", "&f&lSpeed III", "&7Right click to receive speed 3 for 3 seconds.", XMaterial.SUGAR, PotionEffectType.SPEED, 4, 3)
        interactAbilities["portable_jump"] = PortableBardAbility("portable_jump", "&e&lJump Boost VII", "&7Right click to receive jump boost 7 for 3 seconds.", XMaterial.FEATHER, PotionEffectType.JUMP, 8, 3)
        interactAbilities["portable_regen"] = PortableBardAbility("portable_regen", "&d&lRegeneration III", "&7Right click to receive regeneration 3 for 3 seconds.", XMaterial.GHAST_TEAR, PotionEffectType.REGENERATION, 4, 3)

        interactAbilities.forEach {
            cooldowns[it.key] = mutableMapOf()
        }

        damageAbilities.forEach {
            cooldowns[it.key] = mutableMapOf()
        }
    }
}