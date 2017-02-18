package com.richodemus.chronicler.server.dropwizard

import com.codahale.metrics.health.HealthCheck
import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck

internal class SomeHealthcheck : NamedHealthCheck() {
    override fun getName() = "SomeHealthcheck"
    override fun check(): HealthCheck.Result = Result.healthy("Everything is A-OK")
}
