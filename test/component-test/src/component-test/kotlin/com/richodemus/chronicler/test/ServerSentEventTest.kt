package com.richodemus.chronicler.test

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class ServerSentEventTest : DropwizardTest() {
    @Test
    fun `Should send new event to listeners`() {
        val sseClient = getClient().createSSEClient()

        val event = createEvent("uuid", "data")
        val statusCode = getClient().addEvent(event)
        Assertions.assertThat(statusCode).isEqualTo(200)

        sseClient.awaitOneEvent()

        assertThat(sseClient.events).hasSize(1)
        val result = sseClient.events.single()
        assertThat(result).isEqualTo("uuid")
    }

    @Test
    fun `Try sending a hundred events to a hundred listeners`() {
        val count = 100
        val listeners = IntRange(1, count).map { getClient().createSSEClient() }

        val events = IntRange(1, count).map { createEvent(it.toString(), "asd") }

        println("Preparation done, time to send...")
        events.forEach {
            val statusCode = getClient().addEvent(it)
            Assertions.assertThat(statusCode).isEqualTo(200)
        }

        println("$count events sent, time to assert...")
        listeners.forEach { it.awaitEvents(count) }

        val ids = listeners.flatMap { it.events }
        assertThat(ids).hasSize(count * count)

        listeners.forEach {
            assertThat(it.areEventsInOrder()).isTrue()
        }
    }
}
