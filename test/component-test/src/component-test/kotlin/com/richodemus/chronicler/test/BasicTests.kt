package com.richodemus.chronicler.test

import com.richodemus.chronicler.server.api.model.Event
import com.richodemus.chronicler.test.util.InprocessChronicleApplication
import com.richodemus.chronicler.test.util.TestClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class BasicTests {
    private var server: InprocessChronicleApplication? = null
    private var target: TestClient? = null

    @Before
    fun setUp() {
        server = InprocessChronicleApplication()
        server!!.start()
        target = TestClient(server!!.port())
    }

    @After
    fun tearDown() {
        server!!.stop()
    }

    @Test
    fun `Newly started Chronicle shouldn't have any Events`() {
        val result = target!!.getAllEvents()
        assertThat(result).isEmpty()
    }

    @Test
    fun `Add event without specific page`() {
        val event = Event().apply {
            id = "my-cool-id"
        }
        val statusCode = target!!.addEvent(event)
        assertThat(statusCode).isEqualTo(200)

        val result = target!!.getAllEvents()

        assertThat(result).extracting { it.id }.containsOnly("my-cool-id")
    }
}