package com.richodemus.chronicler.server.core


class Chronicle {
    private val events: MutableList<EventHolder> = mutableListOf()

    fun addEvent(expectedVersion: Version, event: Event) {
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

    fun getEvents() = events //todo return immutable copy

    data class EventHolder(val version: NumericalVersion, val event: Event)
}