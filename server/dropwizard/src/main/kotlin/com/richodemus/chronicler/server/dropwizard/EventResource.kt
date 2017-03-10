package com.richodemus.chronicler.server.dropwizard

import com.fasterxml.jackson.databind.ObjectMapper
import com.richodemus.chronicler.server.api.api.EventsApi
import com.richodemus.chronicler.server.api.model.Event
import com.richodemus.chronicler.server.core.Chronicle
import java.math.BigDecimal
import javax.inject.Inject
import javax.ws.rs.core.Response

class EventResource @Inject constructor(val eventConsumer: Chronicle) : EventsApi() {
    override fun eventsGet(ids: MutableList<String>?): Response {

        val events = eventConsumer.getEvents().map { Event().apply { id = "hej-id" } }

        // Why do I need to do this manually?
        val string = ObjectMapper().writeValueAsString(events)

        return string.let { Response.ok(it).build() }
    }

    override fun eventsPageGet(page: Long?): Response {
        return super.eventsPageGet(page)
    }

    override fun eventsPagePut(page: Long?, body: Event?): Response {
        return super.eventsPagePut(page, body)
    }

    override fun eventsPost(): Response {
        return super.eventsPost()
    }

    private fun Long.toBigDecimal() = BigDecimal.valueOf(this)
}
