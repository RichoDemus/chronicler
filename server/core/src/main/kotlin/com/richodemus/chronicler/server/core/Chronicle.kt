package com.richodemus.chronicler.server.core

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Chronicle
@Inject constructor(val listener: EventCreationListener, val persister: EventPersister) {

    private val ids: MutableList<String> = mutableListOf()
    private val events: MutableList<Event> = mutableListOf()
    internal val page: Long
        get() = events.lastOrNull()?.page ?: 0L

    fun addEvent(event: Event) {
        synchronized(events) {
            if (ids.contains(event.id)) {
                return
            }

            val nextPage = calculateNextPage()

            if (event.page != null) {
                if (event.page != nextPage) {
                    throw WrongPageException("Wrong page: ${event.page} for event ${event.id}, correct new page is $nextPage")
                }
            }

            val insertedEvent = event.copy(page = nextPage)
            persister.persist(insertedEvent)
            ids.add(event.id)
            // rest can be asynch, I think
            events.add(insertedEvent)
            listener.onEvent(insertedEvent)
        }
    }

    private fun calculateNextPage() = page + 1L

    fun getEvents(): List<Event> = events
}
