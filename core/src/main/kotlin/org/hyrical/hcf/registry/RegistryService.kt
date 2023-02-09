package org.hyrical.hcf.registry

import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.registry.impl.CommandRegistry
import org.hyrical.hcf.registry.impl.ListenerRegistry
import org.hyrical.hcf.utils.reflection.ClassScanner

object RegistryService {
    val registries = mutableListOf<Registry>()

    fun enable() {
        registries.add(CommandRegistry())
        registries.add(ListenerRegistry())

        registries.forEach {
            val scanner = ClassScanner.getClassesInPackage(
                HCFPlugin.instance,
                "org.hyrical.hcf",
                it.getAnnotation(),
                it.getParent()
            )

            scanner.forEach { shush ->
                it.register(shush.kotlin.objectInstance as Any)
            }
        }
    }
}