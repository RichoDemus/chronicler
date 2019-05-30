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
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.atomic.LongAdder
import java.util.function.Supplier


class GoogleCloudStoragePersistence : EventPersister {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val project = System.getProperty("chronicler.gcs.project")
            ?: throw IllegalArgumentException("Missing property GCS_PROJECT/chronicler.gcs.project")

    private val bucket = System.getProperty("chronicler.gcs.bucket")
            ?: throw IllegalArgumentException("Missing property GCS_BUCKET/chronicler.gcs.bucket")
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

    override fun readEvents(): Iterator<Event> {
        val executor = Executors.newCachedThreadPool()
        var run = true
        val eventsStarted = LongAdder()
        val eventsDownloaded = LongAdder()
        try {
            logger.info("Preparing to download events from Google Cloud Storage...")

            Thread(Runnable {
                while (run) {
                    if (eventsStarted.sum() > 0 || eventsDownloaded.sum() > 0)
                        logger.info("Event downloads started: ${eventsStarted.sum()}, Events downloaded: ${eventsDownloaded.sum()}")
                    Thread.sleep(1_000L)
                }
            }).start()

            return service.list(bucket)
                    .iterateAll()
                    .filter { it.blobId.name.startsWith(directory) }
                    .map {
                        eventsStarted.increment()
                        CompletableFuture.supplyAsync(Supplier {
                            it.getContent()
                                    .let { String(it) }
                                    .toDto()
                                    .toEvent()
                        }, executor)
                    }
                    .map {
                        eventsDownloaded.increment()
                        it.get()
                    }
                    .sortedBy { it.page }
                    .iterator()

        } finally {
            run = false
            executor.shutdown()
        }
    }

    override fun persist(event: Event) {
        service.create(BlobInfo.newBuilder(BlobId.of(bucket, "$directory${event.page.toString()}")).build(), event.toDto().toJSONString().toByteArray())
    }

    private data class EventDTO
    @JsonCreator constructor(
            @JsonProperty("id") val id: String,
            @JsonProperty("type") val type: String,
            @JsonProperty("page") val page: Long,
            @JsonProperty("data") val data: String
    )

    private fun Event.toDto(): EventDTO {
        val page = this.page ?: throw IllegalStateException("Can't save event without page")
        return EventDTO(this.id, this.type, page, this.data)
    }

    private fun GoogleCloudStoragePersistence.EventDTO.toEvent() = com.richodemus.chronicler.server.core.Event(this.id, this.type, this.page, this.data)

    private fun GoogleCloudStoragePersistence.EventDTO.toJSONString() = mapper.writeValueAsString(this)

    private fun String.toDto() = mapper.readValue(this, EventDTO::class.java)
}
