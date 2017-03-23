package com.richodemus.chronicler.server.configuration

import com.richodemus.chronicler.server.core.Configuration
import org.slf4j.LoggerFactory
import java.io.File

internal class JavaPropertyConfiguration : Configuration {
    private val SAVE_TO_DISK_KEY = "chronicler.saveToDisk"
    private val DATA_DIR_KEY = "chronicler.dataDir"
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        logger.info("Save to disk is ${System.getProperty(SAVE_TO_DISK_KEY)}")
        if (!saveToDisk()) {
            logger.warn("$SAVE_TO_DISK_KEY is false, will not persist events!")
        } else {
            logger.info("Will write events to dir \"${dataDirectory()}\"")
        }
    }

    override fun saveToDisk(): Boolean {
        val prop = System.getProperty(SAVE_TO_DISK_KEY, "true")
        if (prop == "") {
            return true
        }
        return prop.toBoolean()
    }

    override fun dataDirectory(): File {
        val prop = System.getProperty(DATA_DIR_KEY, "data/")
        if (prop == "") {
            return "data/".toFile()
        }
        return prop.toFile()
    }

    private fun String.toFile() = File(this)
}
