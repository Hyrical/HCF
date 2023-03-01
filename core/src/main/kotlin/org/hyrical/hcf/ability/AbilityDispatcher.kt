package org.hyrical.hcf.ability

import org.bukkit.event.Event
import org.bukkit.event.Listener

interface AbilityDispatcher<T : Event> : Listener {

    fun dispatch(event: T)
}