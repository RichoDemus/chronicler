package com.richodemus.chronicler.server.dropwizard.inject

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import com.richodemus.chronicler.persistence.DiskEventPersister
import com.richodemus.chronicler.persistence.gcs.GoogleCloudStoragePersistence
import com.richodemus.chronicler.server.core.EventPersister

class ChroniclerModule : AbstractModule() {
    override fun configure() {
        val storage = System.getProperty("chronicler.storage", "DISK")
        when (storage.toUpperCase()) {
            "DISK" -> bind(EventPersister::class.java).to(DiskEventPersister::class.java)
            "GCS" -> bind(EventPersister::class.java).to(GoogleCloudStoragePersistence::class.java)
            else -> throw IllegalArgumentException("Unsupported storage type \"$storage\"")
        }

        bind(EventPersister::class.java).annotatedWith(Names.named("gcs")).to(GoogleCloudStoragePersistence::class.java)
        bind(EventPersister::class.java).annotatedWith(Names.named("disk")).to(DiskEventPersister::class.java)
    }
}