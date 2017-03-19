package com.richodemus.chronicler.server.dropwizard

import com.richodemus.chronicler.server.core.Event
import com.richodemus.chronicler.server.core.EventCreationListener
import org.glassfish.jersey.media.sse.EventOutput
import org.glassfish.jersey.media.sse.OutboundEvent
import org.glassfish.jersey.media.sse.SseFeature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("event-stream")
internal class ServerSentEventResource : EventCreationListener {
    val logger: Logger = LoggerFactory.getLogger(javaClass)
    //todo figure out how to remove disconnected clients
    val outputs = mutableListOf<EventOutput>()

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    fun getServerSentEvents(): EventOutput {
        val output = EventOutput()
        outputs.add(output)
        return output
    }

    //todo investigate jersey sse broadcast
    override fun onEvent(event: Event) {
        logger.debug("Broadcasting event ${event.id} to ${outputs.size} listeners")
        outputs.forEach {
            try {
                val builder = OutboundEvent.Builder()
                builder.data(event.id)
                it.write(builder.build())
            } catch(e: Exception) {
                logger.error("failed sending event to some listener", e)
            }
        }
    }
}
