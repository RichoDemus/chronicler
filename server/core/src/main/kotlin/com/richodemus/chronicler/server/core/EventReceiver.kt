package com.richodemus.chronicler.server.core

import java.util.*

class EventReceiver {
    val events: MutableList<EventHolder> = LinkedList()

    fun consume(expectedVersion: Version, event: Event) {
        synchronized(events) {
            if (events.isEmpty()) {
                events.add(EventHolder(NumericalVersion(1), event))
                return
            }

            val currentVersion = events.last().version
            if (expectedVersion == currentVersion) {
                events.add(EventHolder(currentVersion.next(), event))
                return
            }
            throw IllegalStateException("Wrong version $expectedVersion, current version is $currentVersion")
        }
    }

    fun getVersion() = events.lastOrNull()?.version ?: NumericalVersion(0)

    data class EventHolder(val version: NumericalVersion, val event: Event)
}
