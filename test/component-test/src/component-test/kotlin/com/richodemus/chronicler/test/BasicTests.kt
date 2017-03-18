package com.richodemus.chronicler.test

import com.richodemus.chronicler.server.api.model.Event
import com.richodemus.chronicler.test.util.InprocessChronicleApplication
import com.richodemus.chronicler.test.util.TestClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

internal class BasicTests {
    private val ID = "uuid"

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
            id = ID
        }
        val statusCode = target!!.addEvent(event)
        assertThat(statusCode).isEqualTo(200)

        val results = target!!.getAllEvents()

        assertThat(results).hasSize(1)
        val result = results[0]

        assertThat(result.id).isEqualTo(ID)
        assertThat(result.page).isEqualTo(1L.toBigDecimal())
    }

    @Test
    fun `Add event at specific page`() {
        val event = Event().apply {
            id = ID
        }
        val statusCode = target!!.addEvent(event, 1)
        assertThat(statusCode).isEqualTo(200)

        val results = target!!.getAllEvents()

        assertThat(results).hasSize(1)
        val result = results[0]

        assertThat(result.id).isEqualTo(ID)
        assertThat(result.page).isEqualTo(1L.toBigDecimal())
    }

    @Test
    fun `Adding event at the wrong page should not work`() {
        val event = Event().apply {
            id = ID
        }
        val statusCode = target!!.addEvent(event, 2)
        assertThat(statusCode).isEqualTo(400)
    }

    @Test
    fun `Should not insert duplicate events`() {
        val event = Event().apply {
            id = ID
        }
        var statusCode = target!!.addEvent(event)
        assertThat(statusCode).isEqualTo(200)
        statusCode = target!!.addEvent(event)
        assertThat(statusCode).isEqualTo(200)

        val results = target!!.getAllEvents()
        assertThat(results).hasSize(1)
    }

    private fun Long.toBigDecimal() = BigDecimal.valueOf(this)
}

