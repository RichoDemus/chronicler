package com.richodemus.chronicler.server.persistence

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.richodemus.chronicler.server.core.Event
import java.io.File
import javax.inject.Inject

internal class DiskEventPersister @Inject constructor(val configuration: com.richodemus.chronicler.server.core.Configuration) : com.richodemus.chronicler.server.core.EventPersister {
    init {
        if (!configuration.dataDirectory().exists()) {
            configuration.dataDirectory().mkdirs()
        }
    }

    override fun readEvents(): Iterator<com.richodemus.chronicler.server.core.Event> {
        if (!configuration.saveToDisk()) {
            return listOf<com.richodemus.chronicler.server.core.Event>().iterator()
        }
        return object : Iterator<com.richodemus.chronicler.server.core.Event> {
            private var nextPage = 1L

            override fun hasNext() = File(configuration.dataDirectory(), nextPage.toString()).exists()

            override fun next(): com.richodemus.chronicler.server.core.Event {
                val eventDiskDTO = ObjectMapper().readValue(File(configuration.dataDirectory(), nextPage.toString()), EventDiskDTO::class.java)
                nextPage++
                return eventDiskDTO.toEvent()
            }

        }
    }

    override fun persist(event: com.richodemus.chronicler.server.core.Event) {
        if (!configuration.saveToDisk()) {
            return
        }
        val path = File(configuration.dataDirectory(), event.page.toString())
        ObjectMapper().writeValue(path, event.toDto())
    }

    private data class EventDiskDTO
    @JsonCreator constructor(@JsonProperty("id") val id: String,
                             @JsonProperty("page") val page: Long,
                             @JsonProperty("data") val data: String)

    private fun Event.toDto(): EventDiskDTO {
        val page = this.page ?: throw IllegalStateException("Can't save event without page")
        return EventDiskDTO(this.id, page, this.data)
    }

    private fun DiskEventPersister.EventDiskDTO.toEvent() = Event(this.id, this.page, this.data)
}