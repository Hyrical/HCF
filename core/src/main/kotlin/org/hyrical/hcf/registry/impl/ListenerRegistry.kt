package org.hyrical.hcf.registry.impl

import org.bukkit.Bukkit
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.registry.Registry
import org.hyrical.hcf.registry.annotations.Listener

class ListenerRegistry : Registry {

    override fun getAnnotation(): Class<out Annotation> {
        return Listener::class.java
    }

    override fun getParent(): Class<*> {
        return org.bukkit.event.Listener::class.java
    }

    override fun register(t: Any) {
        Bukkit.getPluginManager().registerEvents(t as org.bukkit.event.Listener, HCFPlugin.instance)
    }
}