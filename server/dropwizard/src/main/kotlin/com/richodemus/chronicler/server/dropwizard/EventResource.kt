package com.richodemus.chronicler.server.dropwizard

import com.fasterxml.jackson.databind.ObjectMapper
import com.richodemus.chronicler.server.api.api.EventTypesApi
import com.richodemus.chronicler.server.api.model.EventType
import com.richodemus.chronicler.server.core.EventReceiver
import java.math.BigDecimal
import javax.inject.Inject
import javax.ws.rs.core.Response

class EventResource @Inject constructor(val eventConsumer: EventReceiver) : EventTypesApi() {
    override fun eventTypesGet(): Response {
        val eventType = EventType().apply {
            id = "default"
            version = eventConsumer.getVersion().version.toBigDecimal()
        }

        // Why do I need to do this manually?
        val string = ObjectMapper().writeValueAsString(listOf(eventType))

        return string.let { Response.ok(it).build() }
    }

    private fun Long.toBigDecimal() = BigDecimal.valueOf(this)
}
