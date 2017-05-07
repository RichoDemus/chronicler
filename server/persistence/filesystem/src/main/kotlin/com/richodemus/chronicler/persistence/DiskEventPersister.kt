package com.richodemus.chronicler.persistence

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.richodemus.chronicler.server.core.Event
import com.richodemus.chronicler.server.core.EventPersister
import java.io.File
import javax.inject.Inject

class DiskEventPersister @Inject internal constructor(val configuration: com.richodemus.chronicler.server.core.Configuration) : EventPersister {
    init {
        if (!configuration.dataDirectory().exists()) {
            configuration.dataDirectory().mkdirs()
        }
    }

    override fun readEvents(): Iterator<Event> {
        if (!configuration.saveToDisk()) {
            return listOf<Event>().iterator()
        }
        return object : Iterator<Event> {
            private var nextPage = 1L

            override fun hasNext() = File(configuration.dataDirectory(), nextPage.toString()).exists()

            override fun next(): Event {
                val eventDiskDTO = ObjectMapper().readValue(File(configuration.dataDirectory(), nextPage.toString()), EventDiskDTO::class.java)
                nextPage++
                return eventDiskDTO.toEvent()
            }

        }
    }

    override fun persist(event: Event) {
        if (!configuration.saveToDisk()) {
            return
        }
        val path = File(configuration.dataDirectory(), event.page.toString())
        ObjectMapper().writeValue(path, event.toDto())
    }

    private data class EventDiskDTO
    @JsonCreator constructor(@JsonProperty("id") val id: String,
                             @JsonProperty("type") val type: String,
                             @JsonProperty("page") val page: Long,
                             @JsonProperty("data") val data: String)

    private fun Event.toDto(): EventDiskDTO {
        val page = this.page ?: throw IllegalStateException("Can't save event without page")
        return EventDiskDTO(this.id, this.type, page, this.data)
    }

    private fun EventDiskDTO.toEvent() = Event(this.id, this.type, this.page, this.data)
}
