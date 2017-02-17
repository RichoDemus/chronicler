package com.richodemus.chronicler.server.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EventTest {
    @Test
    fun `Test test please remove`() {
        assertThat(Event("id")).isEqualTo(Event("id"))
    }
}
