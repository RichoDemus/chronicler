package com.richodemus.chronicler.server.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class ChronicleTest {
    private val id = "uuid"

    @Test
    fun `New Chronicle should be empty`() {
        val result = Chronicle().getEvents()
        assertThat(result).hasSize(0)
    }

    @Test
    fun `New Chronicle should be at page zero`() {
        val result = Chronicle().page
        assertThat(result).isEqualTo(0L)
    }

    @Test
    fun `A Chronicle with one event should return one event`() {
        val target = Chronicle()
        target.addEvent(Event(id, 1L))

        assertThat(target.getEvents()).hasSize(1)
    }

    @Test
    fun `A Chronicle with one event should be at page one`() {
        val target = Chronicle()
        target.addEvent(Event(id, 1L))

        assertThat(target.page).isEqualTo(1L)
    }

    @Test
    fun `Add pageless Event to empty Chronicle`() {
        val target = Chronicle()

        target.addEvent(Event(id, null))

        val result = target.getEvents()[0]
        assertThat(result.id).isEqualTo(id)
        assertThat(result.page).isEqualTo(1L)
    }
}
