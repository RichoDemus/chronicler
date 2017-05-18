package com.richodemus.chronicler.server.core

import org.slf4j.LoggerFactory
import java.time.Duration
import kotlin.system.measureTimeMillis

class EventLoader(val persister: EventPersister) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getEvents(): List<Event> {
        logger.info("Loading events...")
        val events = mutableListOf<Event>()

        val time = measureTimeMillis {
            val eventIterator = persister.readEvents()
            eventIterator.forEach {
                events.add(it)
            }
        }
        val duration = Duration.ofMillis(time)
        val durationString = String.format("%d minutes and %d seconds", (duration.seconds % 3600) / 60, (duration.seconds % 60))
        logger.info("Loaded ${events.size} events in $durationString")
        return events
    }
}
