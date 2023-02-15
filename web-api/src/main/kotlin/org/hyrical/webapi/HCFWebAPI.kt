package org.hyrical.webapi

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.hyrical.webapi.commands.CreateCommand
import org.hyrical.webapi.plugins.configureMonitoring
import org.hyrical.webapi.plugins.configureRouting
import revxrsal.commands.cli.ConsoleCommandHandler
import kotlin.concurrent.thread

val COMMAND_HANDLER = ConsoleCommandHandler.create()

fun main() {
    thread {
        COMMAND_HANDLER.register(CreateCommand())
        COMMAND_HANDLER.pollInput()
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureMonitoring()
    configureRouting()

}
