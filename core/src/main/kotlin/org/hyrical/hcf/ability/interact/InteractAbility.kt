package org.hyrical.hcf.ability.interact

import org.bukkit.event.player.PlayerInteractEvent;
import org.hyrical.hcf.ability.Ability

abstract class InteractAbility(override val id: String) : Ability<PlayerInteractEvent>(id)