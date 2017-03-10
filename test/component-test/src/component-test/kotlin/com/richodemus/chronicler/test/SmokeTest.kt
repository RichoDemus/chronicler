package com.richodemus.chronicler.test

import com.richodemus.chronicler.test.util.ChroniclerTestClient
import com.richodemus.chronicler.test.util.InprocessChronicleApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class SmokeTest {
    private var server: InprocessChronicleApplication? = null
    private var target: ChroniclerTestClient? = null

    @Before
    fun setUp() {
        server = InprocessChronicleApplication()
        server!!.start()
        target = ChroniclerTestClient(server!!.port())
    }

    @After
    fun tearDown() {
        server!!.stop()
    }

    @Test
    fun test() {
        val result = target!!.mainPage()
        assertThat(result).isEqualTo(200)
    }

    @Test
    fun `Make sure the swagger page is present`() {
        val result = target!!.swaggerPage()
        assertThat(result).containsIgnoringCase("swagger")
    }
}
