package com.richodemus.chronicler.persistence.gcs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageOptions
import com.richodemus.chronicler.server.core.Event
import com.richodemus.chronicler.server.core.EventPersister
import javax.inject.Singleton

@Singleton
class GoogleCloudStoragePersistence : EventPersister {
    private val project = System.getProperty("chronicler.gcs.project") ?: throw IllegalArgumentException("Missing property GCS_PROJECT/chronicler.gcs.project")

    private val bucket = System.getProperty("chronicler.gcs.bucket") ?: throw IllegalArgumentException("Missing property GCS_BUCKET/chronicler.gcs.bucket")
    private val directory = "events/v1/"
    private val mapper = ObjectMapper().apply { registerModule(KotlinModule()) }

    private val service = StorageOptions.newBuilder()
            .setProjectId(project)
            .build()
            .service

    override fun getNumberOfEvents(): Int {
        return service.list(bucket)
                    .iterateAll().count()
    }

    override fun readEvents() = service.list(bucket)
            .iterateAll()
            .filter { it.blobId.name.startsWith(directory) }
            .map { it.getContent() }
            .map { String(it) }
            .map { it.toDto() }
            .map { it.toEvent() }
            .sortedBy { it.page }
            .iterator()

    override fun persist(event: Event) {
        service.create(BlobInfo.newBuilder(BlobId.of(bucket, "$directory${event.page.toString()}")).build(), event.toDto().toJSONString().toByteArray())
    }

    private data class EventDTO
    @JsonCreator constructor(@JsonProperty("id") val id: String,
                             @JsonProperty("type") val type: String,
                             @JsonProperty("page") val page: Long,
                             @JsonProperty("data") val data: String)

    private fun Event.toDto(): EventDTO {
        val page = this.page ?: throw IllegalStateException("Can't save event without page")
        return EventDTO(this.id, this.type, page, this.data)
    }

    private fun GoogleCloudStoragePersistence.EventDTO.toEvent() = com.richodemus.chronicler.server.core.Event(this.id, this.type, this.page, this.data)

    private fun GoogleCloudStoragePersistence.EventDTO.toJSONString() = mapper.writeValueAsString(this)

    private fun String.toDto() = mapper.readValue(this, EventDTO::class.java)
}
