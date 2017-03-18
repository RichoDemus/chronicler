package com.richodemus.chronicler.server.core

interface EventCreationListener {
    fun onEvent(event: Event)
}
