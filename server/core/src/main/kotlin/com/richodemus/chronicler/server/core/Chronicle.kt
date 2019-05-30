package com.richodemus.chronicler.server.core

import org.slf4j.LoggerFactory

class Chronicle
constructor(val listener: EventCreationListener, val persister: EventPersister) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var ready = false
    private val ids: MutableList<String> = mutableListOf()
    private val events: MutableList<Event> = mutableListOf()
    internal val page: Long
        get() = events.lastOrNull()?.page ?: 0L

    init {
        events.addAll(EventLoader(persister).getEvents())
        ids.addAll(events.map { it.id })
        ready = true
        logger.info("Chronicler started and ready")
    }

    fun addEvent(event: Event) {
        if (!ready) {
            throw IllegalStateException("Chronicler still starting...")
        }
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
    internal fun getIds(): List<String> = ids
}
