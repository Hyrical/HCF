package org.hyrical.hcf.registry.impl

import co.aikar.commands.BaseCommand
import org.hyrical.hcf.registry.Registry
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.registry.annotations.Command

class CommandRegistry : Registry {

    override fun getAnnotation(): Class<out Annotation> {
        return Command::class.java
    }

    override fun getParent(): Class<*> {
        return BaseCommand::class.java
    }

    override fun register(t: Any) {
        HCFPlugin.instance.commandManager.registerCommand(t as BaseCommand)
    }
}
