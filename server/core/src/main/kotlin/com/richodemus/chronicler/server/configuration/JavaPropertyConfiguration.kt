package com.richodemus.chronicler.server.configuration

import com.richodemus.chronicler.server.core.Configuration
import java.io.File

internal class JavaPropertyConfiguration : Configuration {
    private val SAVE_TO_DISK_KEY = "chronicler.saveToDisk"
    private val DATA_DIR_KEY = "chronicler.dataDir"

    override fun saveToDisk() = System.getProperty(SAVE_TO_DISK_KEY, "true").toBoolean()

    override fun dataDirectory() = System.getProperty(DATA_DIR_KEY, "data/").toFile()

    private fun String.toFile() = File(this)
}
