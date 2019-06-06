package com.richodemus.chronicler.test


import com.richodemus.chronicler.server.koin.MyCoin
import com.richodemus.chronicler.server.koin.Tezt
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

internal class SmokeTest {

    @BeforeEach
    internal fun setUp() {
        startKoin {
            MyCoin.Module
        }
    }

    @AfterEach
    internal fun tearDown() {
        stopKoin()
    }

    @Test
    internal fun `simple smoke test`() {
        val test = Tezt()
        println(test)
        println()
    }
}
