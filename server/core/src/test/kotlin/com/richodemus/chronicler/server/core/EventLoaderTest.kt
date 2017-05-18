package com.richodemus.chronicler.server.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class EventLoaderTest {
    @Test
    fun `Load Zero Events`() {
        val mock = PersisterMock()

        val target = EventLoader(mock)

        val result = target.getEvents()
        assertThat(result).isEmpty()
    }

    @Test
    fun `Load Events`() {
        val mock = PersisterMock()
        IntRange(0, 10_000)
                .map { Event(it.toString(), it.toString(), it.toLong(), it.toString()) }
                .forEach { mock.persist(it) }

        val target = EventLoader(mock)

        val result = target.getEvents()
        assertThat(result).isNotEmpty
    }

    class PersisterMock : EventPersister {
        val events = mutableListOf<Event>()

        override fun readEvents(): Iterator<Event> {
            return events.iterator()
        }

        override fun persist(event: Event) {
            events.add(event)
        }
    }
}
