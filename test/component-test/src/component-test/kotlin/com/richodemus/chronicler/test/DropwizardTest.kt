package com.richodemus.chronicler.test

import com.richodemus.chronicler.server.api.model.EventWithoutPage
import com.richodemus.chronicler.test.util.InprocessChronicleApplication
import com.richodemus.chronicler.test.util.TestClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before

internal abstract class DropwizardTest {

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

    protected fun getClient() = target!!

    protected fun createEvent(id: String, data: String): EventWithoutPage {
        return EventWithoutPage().apply {
            this.id = id
            this.data = data
        }
    }

    protected fun sendEvent(event: EventWithoutPage) {
        assertThat(getClient().addEvent(event)).isEqualTo(200)
    }

    protected fun sendEvent(event: EventWithoutPage, page: Int) {
        assertThat(getClient().addEvent(event, page)).isEqualTo(200)
    }
}