package org.hyrical.webapi.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/api/licence/{identifier}") {
            val identifier = call.parameters["identifier"] ?: return@get call.respondText("No identifier provided", status = io.ktor.http.HttpStatusCode.BadRequest)
            org.hyrical.webapi.storage.StorageHandler.getLicence(identifier) ?: return@get call.respondText("No licence found for identifier $identifier", status = io.ktor.http.HttpStatusCode.NotFound)
            call.respond(
                "Success"
            )
        }
    }
}
