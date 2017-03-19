package com.richodemus.chronicler.test

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal

internal class BasicTests : DropwizardTest() {
    private val ID = "uuid"
    private val DATA = "Lots of data"

    @Test
    fun `Newly started Chronicle shouldn't have any Events`() {
        val result = getClient().getAllEvents()
        assertThat(result).isEmpty()
    }

    @Test
    fun `Add event without specific page`() {
        val event = createEvent(ID, DATA)
        val statusCode = getClient().addEvent(event)
        assertThat(statusCode).isEqualTo(200)

        val results = getClient().getAllEvents()

        assertThat(results).hasSize(1)
        val result = results.single()

        assertThat(result.id).isEqualTo(ID)
        assertThat(result.page).isEqualTo(1L.toBigDecimal())
        assertThat(result.data).isEqualTo(DATA)
    }

    @Test
    fun `Add event at specific page`() {
        val event = createEvent(ID, DATA)
        val statusCode = getClient().addEvent(event, 1)
        assertThat(statusCode).isEqualTo(200)

        val results = getClient().getAllEvents()

        assertThat(results).hasSize(1)
        val result = results.single()

        assertThat(result.id).isEqualTo(ID)
        assertThat(result.page).isEqualTo(1L.toBigDecimal())
        assertThat(result.data).isEqualTo(DATA)
    }

    @Test
    fun `Adding event at the wrong page should not work`() {
        val event = createEvent(ID, DATA)
        val statusCode = getClient().addEvent(event, 2)
        assertThat(statusCode).isEqualTo(400)
    }

    @Test
    fun `Should not insert duplicate events`() {
        val firstEvent = createEvent(ID, DATA)
        val secondEvent = createEvent(ID, DATA.reversed())
        var statusCode = getClient().addEvent(firstEvent)
        assertThat(statusCode).isEqualTo(200)
        statusCode = getClient().addEvent(secondEvent)
        assertThat(statusCode).isEqualTo(200)

        val results = getClient().getAllEvents()
        assertThat(results).hasSize(1)

        val result = results.single()

        assertThat(result.id).isEqualTo(ID)
        assertThat(result.page).isEqualTo(1L.toBigDecimal())
        assertThat(result.data).isEqualTo(DATA)
    }

    private fun Long.toBigDecimal() = BigDecimal.valueOf(this)
}

