package com.richodemus.chronicler.server.dropwizard

import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("events")
class EventConsumingResource {
    @GET
    fun consume() = listOf("nom", "nom", "nom").joinToString(" ")
}
