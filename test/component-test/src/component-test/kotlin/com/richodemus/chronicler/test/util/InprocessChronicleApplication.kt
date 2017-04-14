package com.richodemus.chronicler.test.util

import com.richodemus.chronicler.server.dropwizard.ChroniclerApplication
import com.richodemus.chronicler.server.dropwizard.ChroniclerConfiguration
import io.dropwizard.testing.ConfigOverride
import io.dropwizard.testing.DropwizardTestSupport

internal class InprocessChronicleApplication {
    private val support: DropwizardTestSupport<ChroniclerConfiguration> = DropwizardTestSupport(
            ChroniclerApplication::class.java,
            "../../server/docker/config.yaml",
            ConfigOverride.config("server.applicationConnectors[0].port", "0"),
            ConfigOverride.config("server.adminConnectors[0].port", "0"))

    fun start() = support.before()
    fun stop() = support.after()
    fun port() = support.localPort
}
