package com.richodemus.chronicler.server.core


class Chronicle {
    private val events: MutableList<Event> = mutableListOf()
    internal val page: Long
        get() = events.lastOrNull()?.page ?: 0L

    fun addEvent(event: Event) {
        synchronized(events) {
            val nextPage = calculateNextPage()

            if (event.page != null) {
                if (event.page != nextPage) {
                    throw WrongPageException("Wrong page: ${event.page} for event ${event.id}, correct new page is $nextPage")
                }
            }

            events.add(event.copy(page = nextPage))
        }
    }

    private fun calculateNextPage() = page + 1L

    fun getEvents() = events //todo return immutable copy
}
