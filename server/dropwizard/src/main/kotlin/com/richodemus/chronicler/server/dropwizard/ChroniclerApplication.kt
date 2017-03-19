package com.richodemus.chronicler.server.dropwizard

import com.github.richodemus.guice_classpath_scanning.ClassPathScanningModule
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import ru.vyarus.dropwizard.guice.GuiceBundle


class ChroniclerApplication : Application<ChroniclerConfiguration>() {
    companion object {
        @JvmStatic fun main(args: Array<String>) = ChroniclerApplication().run(*args)
    }

    override fun initialize(bootstrap: Bootstrap<ChroniclerConfiguration>) {
        bootstrap.addBundle(GuiceBundle.builder<ChroniclerConfiguration>()
                .enableAutoConfig("com.richodemus.chronicler.server.dropwizard")
                .modules(ClassPathScanningModule("com.richodemus.chronicler.server"))
                .build())
        bootstrap.addBundle(AssetsBundle("/webroot/", "/", "index.html", "static"))
        bootstrap.addBundle(object : SwaggerBundle<ChroniclerConfiguration>() {
            override fun getSwaggerBundleConfiguration(configuration: ChroniclerConfiguration): SwaggerBundleConfiguration {
                return configuration.swaggerBundleConfiguration
            }
        })
    }

    override fun run(configuration: ChroniclerConfiguration, environment: Environment) {
    }
}
