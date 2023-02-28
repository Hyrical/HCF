package org.hyrical.hcf.ability.interact

import org.bukkit.event.player.PlayerInteractEvent;
import org.hyrical.hcf.ability.Ability

abstract class InteractAbility(private val ID: String) : Ability(ID) {

    abstract fun handle(event: PlayerInteractEvent)

}