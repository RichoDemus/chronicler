package com.richodemus.chronicler.server.dropwizard

import com.fasterxml.jackson.databind.ObjectMapper
import com.richodemus.chronicler.server.api.api.EventsApi
import com.richodemus.chronicler.server.api.model.Event
import com.richodemus.chronicler.server.api.model.EventWithoutPage
import com.richodemus.chronicler.server.core.Chronicle
import com.richodemus.chronicler.server.core.WrongPageException
import java.math.BigDecimal
import javax.inject.Inject
import javax.ws.rs.core.Response

internal class EventResource @Inject constructor(val chronicle: Chronicle) : EventsApi() {
    override fun eventsGet(ids: MutableList<String>?): Response {
        val events = chronicle.getEvents().map { it.toDtoEvent() }

        // Why do I need to do this manually?
        val string = ObjectMapper().writeValueAsString(events)

        return string.let { Response.ok(it).build() }
    }

    override fun eventsPageGet(page: Long?): Response {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
    }

    /**
     * Used when specifying a page exactly
     */
    override fun eventsPagePut(page: Long?, event: EventWithoutPage?): Response {
        if (event == null || page == null) {
            //todo can this even happen?
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
        try {
            chronicle.addEvent(com.richodemus.chronicler.server.core.Event(event.id, event.type, page, event.data))
            return Response.ok().build()
        } catch(e: WrongPageException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
        }
    }

    /**
     * Used when not caring about which page number is assigned, i.e. not caring about being up to date
     */
    override fun eventsPost(event: EventWithoutPage?): Response {
        if (event == null) {
            //todo can this even happen?
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
        try {
            chronicle.addEvent(com.richodemus.chronicler.server.core.Event(event.id, event.type, null, event.data))
            return Response.ok().build()
        } catch(e: WrongPageException) {
            //language=JSON
            val msg = "{\"msg\":\"${e.message}\"}"
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).build()
        }
    }

    private fun com.richodemus.chronicler.server.core.Event.toDtoEvent(): Event {
        val coolId = this.id
        val coolType = this.type
        val coolPage = this.page ?: throw IllegalStateException("Got a pageless event from the event store, this shouldn't be possible :O")
        val coolData = this.data
        return Event().apply {
            id = coolId
            type = coolType
            page = coolPage.toBigDecimal()
            data = coolData
        }
    }

    private fun Long.toBigDecimal() = BigDecimal.valueOf(this)
}
