package com.richodemus.chronicler.server.core

import org.slf4j.LoggerFactory
import java.time.Duration
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class Chronicle
@Inject constructor(val listener: EventCreationListener, @Named("disk") val persister: EventPersister, @Named("gcs") val gcsPersister: EventPersister) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var ready = false
    private val ids: MutableList<String> = mutableListOf()
    private val events: MutableList<Event> = mutableListOf()
    internal val page: Long
        get() = events.lastOrNull()?.page ?: 0L

    init {
        logger.info("Loading events...")
        val time = measureTimeMillis {
            val eventIterator = persister.readEvents()
            eventIterator.forEach {
                events.add(it)
                ids.add(it.id)
            }
        }
        val duration = Duration.ofMillis(time)
        val durationString = String.format("%d minutes and %d seconds", (duration.seconds % 3600) / 60, (duration.seconds % 60))
        logger.info("Loaded ${ids.size} events in $durationString")
        ready = true

//        events.forEachIndexed { i, event ->
//            gcsPersister.persist(event)
//            if(i.mod(100) == 0) {
//                print(".")
//            }
//        }
//        logger.info("Migrated ${events.size} events...")
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
