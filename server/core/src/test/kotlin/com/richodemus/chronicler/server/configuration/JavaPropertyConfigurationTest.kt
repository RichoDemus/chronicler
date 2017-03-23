package com.richodemus.chronicler.server.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class JavaPropertyConfigurationTest {
    @Test
    fun `Should return true if disk persistence is set to empty string`() {
        System.setProperty("chronicler.saveToDisk", "")

        val result = JavaPropertyConfiguration().saveToDisk()

        assertThat(result).isTrue()

        System.clearProperty("chronicler.saveToDisk")
    }
}
