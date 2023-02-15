package org.hyrical.webapi.commands

import org.hyrical.webapi.storage.StorageHandler
import org.hyrical.webapi.type.Licence
import revxrsal.commands.annotation.Command
import revxrsal.commands.cli.core.CommandLineActor
import java.util.UUID

class CreateCommand {

    @Command("create")
    fun create(actor: CommandLineActor) {
        try {
            // 8 characters
            val identifier = UUID.randomUUID().toString().substring(0, 8)
            actor.reply("Created licence with identifier $identifier")

            StorageHandler.controller.repository.save(
                Licence(identifier)
            )
        } catch (e: Exception) {
            actor.reply("Failed to create licence: ${e.message}")
        }
    }
}