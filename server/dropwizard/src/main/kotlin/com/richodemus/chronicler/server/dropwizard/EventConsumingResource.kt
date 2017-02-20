package com.richodemus.chronicler.server.dropwizard

import com.richodemus.chronicler.server.core.EventReceiver
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("events")
class EventConsumingResource @Inject constructor(val eventConsumer: EventReceiver) {
    @GET
    fun consume(): String = listOf("Current", "version:", eventConsumer.getVersion().version.toString()).joinToString(" ")
}
