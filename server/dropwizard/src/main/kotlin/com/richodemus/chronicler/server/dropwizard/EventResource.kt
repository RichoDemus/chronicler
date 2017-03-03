package com.richodemus.chronicler.server.dropwizard

import com.fasterxml.jackson.databind.ObjectMapper
import com.richodemus.chronicler.server.api.api.EventsApi
import com.richodemus.chronicler.server.api.model.Event
import com.richodemus.chronicler.server.core.Chronicle
import java.math.BigDecimal
import javax.inject.Inject
import javax.ws.rs.core.Response

class EventResource @Inject constructor(val eventConsumer: Chronicle) : EventsApi() {
    override fun eventsGet(): Response {

        val events = eventConsumer.getEvents().map { Event().apply { id = "hej-id" } }

        // Why do I need to do this manually?
        val string = ObjectMapper().writeValueAsString(events)

        return string.let { Response.ok(it).build() }
    }

    private fun Long.toBigDecimal() = BigDecimal.valueOf(this)
}
