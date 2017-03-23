package com.richodemus.chronicler.server.core

interface EventPersister {
    fun readEvents(): Iterator<Event>
    fun persist(event: Event)
}
