package com.richodemus.chronicler.server.dropwizard

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.richodemus.chronicler.server.core.Chronicle
import com.richodemus.chronicler.server.core.Event
import com.richodemus.chronicler.server.core.NumericalVersion
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EventResourceTest {
    @Test
    fun `Some stupid test`() {
        val events = mutableListOf(Chronicle.EventHolder(NumericalVersion(10), Event("hello")))
        val mock = mock<Chronicle> {
            on { getEvents() } doReturn events
        }
        //language=JSON
        val expected = "[{\"id\":\"hej-id\",\"page\":null}]"

        val result = EventResource(mock).eventsGet().entity.toString()

        assertThat(result).isEqualTo(expected)
    }
}