package com.richodemus.chronicler.test.util

import org.glassfish.jersey.media.sse.EventInput
import org.glassfish.jersey.media.sse.SseFeature
import javax.ws.rs.client.ClientBuilder


internal class ServerSentEventClient(baseUrl: String) {
    val events = mutableListOf<String>()
    private val eventInput = ClientBuilder.newBuilder()
            .register(SseFeature::class.java)
            .build()
            .target("$baseUrl/api/event-stream/")
            .request()
            .get(EventInput::class.java)

    init {
        Thread(Runnable {
            try {
                while (!eventInput.isClosed) {
                    val inboundEvent = eventInput.read() ?: break// connection has been closed

                    val id = inboundEvent.readData(String::class.java)
                    onEvent(id)
                }
            } finally {
                println("Event receiving thread done...")
            }
        }).start()
    }

    private fun onEvent(id: String) {
        events.add(id)
    }
}
