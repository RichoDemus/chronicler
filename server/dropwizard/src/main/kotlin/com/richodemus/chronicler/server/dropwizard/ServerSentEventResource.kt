package com.richodemus.chronicler.server.dropwizard

import org.glassfish.jersey.media.sse.EventOutput
import org.glassfish.jersey.media.sse.OutboundEvent
import org.glassfish.jersey.media.sse.SseFeature
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("events")
internal class ServerSentEventResource {

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    fun getServerSentEvents(): EventOutput {
        val output = EventOutput()

        Thread(Runnable {
            try {
                IntRange(1,10).forEach {
                    val builder = OutboundEvent.Builder()
                    builder.name("name?")
                    builder.data("a message $it")
                    output.write(builder.build())
                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            } finally {
                output.close()
            }
        }).start()

        return output
    }
}
