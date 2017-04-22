package com.richodemus.chronicler.server.persistence

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.richodemus.chronicler.server.core.Configuration
import com.richodemus.chronicler.server.core.Event
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File
import java.util.UUID

internal class DiskEventPersisterTest {
    @Test
    fun `Save then load events`() {
        val configurationMock = mock<Configuration> {
            on { saveToDisk() } doReturn true
            on { dataDirectory() } doReturn File("build/dataDirs/${UUID.randomUUID()}")
        }

        val target = DiskEventPersister(configurationMock)

        val event1 = Event("one", "type", 1L, "some data")
        val event2 = Event("two", "type", 2L, "some more data")
        target.persist(event1)
        target.persist(event2)

        val target2 = DiskEventPersister(configurationMock)


        val result = target2.readEvents().asSequence().toList()
        assertThat(result).containsExactly(event1, event2)
    }
}
