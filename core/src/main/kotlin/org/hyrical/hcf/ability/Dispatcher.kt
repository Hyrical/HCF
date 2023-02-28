package org.hyrical.hcf.ability

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent

interface Dispatcher<T : Event> : Listener {

    fun dispatch(event: T)
}