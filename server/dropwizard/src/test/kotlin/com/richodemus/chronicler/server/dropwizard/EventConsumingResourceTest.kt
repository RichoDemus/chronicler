package com.richodemus.chronicler.server.dropwizard

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EventConsumingResourceTest {
    @Test
    fun `Some stupid test`() {
        assertThat(EventConsumingResource().consume()).isEqualTo("nom nom nom")
    }
}
