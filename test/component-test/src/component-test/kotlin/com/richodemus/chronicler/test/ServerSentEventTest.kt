package com.richodemus.chronicler.test

import com.richodemus.chronicler.server.api.model.EventWithoutPage
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.Test
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

internal class ServerSentEventTest : DropwizardTest() {
    @Test
    fun `Should send new event to listeners`() {
        val sseClient = getClient().createSSEClient()

        val event = EventWithoutPage().apply {
            id = "uuid"
            data = "data"
        }
        val statusCode = getClient().addEvent(event)
        Assertions.assertThat(statusCode).isEqualTo(200)

        await().atMost(10, TimeUnit.SECONDS).until(Callable { sseClient.events.isNotEmpty() })

        println("Done closing, asserting...")
        assertThat(sseClient.events).hasSize(1)
        val result = sseClient.events.single()
        assertThat(result).isEqualTo("uuid")
    }
}
