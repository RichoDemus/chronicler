package com.richodemus.chronicler.server.dropwizard

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

class ChroniclerConfiguration(@JsonProperty("swagger") val swaggerBundleConfiguration: SwaggerBundleConfiguration) : Configuration()
