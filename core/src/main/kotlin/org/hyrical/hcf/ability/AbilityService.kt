package org.hyrical.hcf.ability

import com.cryptomorin.xseries.XMaterial
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.ability.damage.DamageAbility
import org.hyrical.hcf.ability.interact.InteractAbility
import org.hyrical.hcf.ability.interact.impl.PortableBardAbility

object AbilityService
{
    val interactAbilities = mutableMapOf<String, InteractAbility>()
    val damageAbilities = mutableMapOf<String, DamageAbility>()

    fun loadAll() {
        interactAbilities["portable_strength"] = PortableBardAbility("portable_strength", "&cStrength II", "&7Right click to receieve strength 2 for 3 seconds.", XMaterial.BLAZE_POWDER, PotionEffectType.INCREASE_DAMAGE, 3, 3)
    }
}