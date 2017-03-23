package com.richodemus.chronicler.server.core

import java.io.File

interface Configuration {
    fun saveToDisk(): Boolean
    fun dataDirectory(): File
}
