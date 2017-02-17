package com.richodemus.chronicler.server.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class VersionTest{
    @Test
    fun `Any should equal any version`() {
        assertThat(AnyVersion()).isEqualTo(AnyVersion())
        assertThat(AnyVersion()).isEqualTo(NumericalVersion(1))
    }

    @Test
    fun `Any version should equal Any`() {
        assertThat(NumericalVersion(1)).isEqualTo(AnyVersion())
    }
}
