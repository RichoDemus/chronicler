package com.richodemus.chronicler.server.dropwizard

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.richodemus.chronicler.server.core.Chronicle
import com.richodemus.chronicler.server.core.Event
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EventResourceTest {
    @Test
    fun `Some stupid test`() {
        val events = mutableListOf(Event("hello", 1L))
        val mock = mock<Chronicle> {
            on { getEvents() } doReturn events
        }
        //language=JSON
        val expected = "[{\"id\":\"hello\",\"page\":1}]"

        val result = EventResource(mock).eventsGet(mutableListOf<String>()).entity.toString()

        assertThat(result).isEqualTo(expected)
    }
}
