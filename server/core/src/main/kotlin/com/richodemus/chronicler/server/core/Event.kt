package com.richodemus.chronicler.server.core

data class Event(val id: String, val type: String, val page: Long?, val data: String)
