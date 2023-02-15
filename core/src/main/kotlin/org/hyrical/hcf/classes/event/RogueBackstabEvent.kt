package org.hyrical.hcf.classes.event

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

class RogueBackstabEvent(val entityPerson: Entity, val backstabbedBy: Player, cause: DamageCause, backStabDamage: Double) : EntityDamageEvent(entityPerson, cause, backStabDamage)