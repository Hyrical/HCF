package org.hyrical.hcf.ability.damage

import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.hyrical.hcf.ability.Ability

abstract class DamageAbility(private val ID: String) : Ability<EntityDamageByEntityEvent>(ID)