package com.richodemus.chronicler.server.core

interface EventPersister {
    fun persist(event: Event)
}