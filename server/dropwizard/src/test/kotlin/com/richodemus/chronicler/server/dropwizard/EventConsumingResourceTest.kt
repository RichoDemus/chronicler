package com.richodemus.chronicler.server.dropwizard

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.richodemus.chronicler.server.core.EventReceiver
import com.richodemus.chronicler.server.core.NumericalVersion
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EventConsumingResourceTest {
    @Test
    fun `Some stupid test`() {
        val mock = mock<EventReceiver> {
            on { getVersion() } doReturn NumericalVersion(10)
        }
        assertThat(EventConsumingResource(mock).consume()).isEqualTo("Current version: 10")
    }
}
