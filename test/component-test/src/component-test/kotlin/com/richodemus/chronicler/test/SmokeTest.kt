package com.richodemus.chronicler.test

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class SmokeTest : DropwizardTest() {

    @Test
    fun `Make sure server responds on http port`() {
        val result = getClient().mainPage()
        assertThat(result).isEqualTo(200)
    }

    @Test
    fun `Make sure the swagger page is present`() {
        val result = getClient().swaggerPage()
        assertThat(result).containsIgnoringCase("swagger")
    }
}
