package com.richodemus.chronicler.test

import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.util.*

internal class PersistenceTest : DropwizardTest() {
    private val ID = "uuid"
    private val DATA = "Lots of data"

    @Before
    override fun setUp() {
        System.setProperty("chronicler.saveToDisk", "true")
        System.setProperty("chronicler.dataDir", "build/saveData/${UUID.randomUUID()}")
        start()
    }

    override fun tearDown() {
        super.tearDown()
        System.clearProperty("chronicler.saveToDisk")
        System.clearProperty("chronicler.dataDir")
    }

    @Test
    fun `Should reload events upon restart`() {
        sendEvent(createEvent(ID, DATA))

        stop()
        start()

        val results = getClient().getAllEvents()

        Assertions.assertThat(results).hasSize(1)
        val result = results.single()

        Assertions.assertThat(result.id).isEqualTo(ID)
        Assertions.assertThat(result.page).isEqualTo(1L.toBigDecimal())
        Assertions.assertThat(result.data).isEqualTo(DATA)
    }
}
