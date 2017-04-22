package com.richodemus.chronicler.test

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class ServerSentEventTest : DropwizardTest() {
    @Test
    fun `Should send new event to listeners`() {
        val sseClient = getClient().createSSEClient()

        sendEvent(createEvent("uuid", "data"))

        sseClient.awaitOneEvent()

        assertThat(sseClient.events).hasSize(1)
        val result = sseClient.events.single()
        assertThat(result.id).isEqualTo("uuid")
    }

    @Test
    fun `Try sending a hundred events to a hundred listeners`() {
        val count = 100
        val listeners = IntRange(1, count).map { getClient().createSSEClient() }

        val events = IntRange(1, count).map { createEvent(it.toString(), "asd") }

        println("Preparation done, time to send...")
        events.forEach {
            sendEvent(it)
        }

        println("$count events sent, time to assert...")
        listeners.forEach { it.awaitEvents(count) }

        val ids = listeners.flatMap { it.events }
        assertThat(ids).hasSize(count * count)

        listeners.forEach {
            assertThat(it.areEventsInOrder()).isTrue()
        }
    }

    @Test
    fun `Should receive all past and future events`() {
        sendEvent(createEvent("1", "data"))
        sendEvent(createEvent("2", "data"))

        val sseClient = getClient().createSSEClient()

        sseClient.awaitEvents(2)

        assertThat(sseClient.events[0].id).isEqualTo("1")
        assertThat(sseClient.events[1].id).isEqualTo("2")

        sendEvent(createEvent("3", "data"))

        sseClient.awaitEvents(3)
        assertThat(sseClient.events[2].id).isEqualTo("3")

        sendEvent(createEvent("4", "data"))

        sseClient.awaitEvents(4)
        assertThat(sseClient.events[3].id).isEqualTo("4")
    }
}
