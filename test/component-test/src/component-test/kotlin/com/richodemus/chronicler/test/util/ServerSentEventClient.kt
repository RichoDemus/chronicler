package com.richodemus.chronicler.test.util

//import com.google.common.collect.Ordering
//import org.awaitility.Awaitility
//import org.glassfish.jersey.media.sse.EventInput
//import org.glassfish.jersey.media.sse.SseFeature
//import java.util.concurrent.Callable
//import java.util.concurrent.TimeUnit
//import javax.ws.rs.client.ClientBuilder
//
//
//internal class ServerSentEventClient(baseUrl: String) {
//    val events = mutableListOf<com.richodemus.chronicler.test.util.Event>()
//    private val eventInput = ClientBuilder.newBuilder()
//            .register(SseFeature::class.java)
//            .build()
//            .target("$baseUrl/api/event-stream/")
//            .request()
//            .get(EventInput::class.java)
//
//    init {
//        Thread(Runnable {
//            while (!eventInput.isClosed) {
//                val inboundEvent = eventInput.read() ?: break// connection has been closed
//
//                val id = inboundEvent.id
//                val data = inboundEvent.readData(String::class.java)
//                onEvent(Event(id, data))
//            }
//        }).start()
//    }
//
//    fun awaitOneEvent() {
//        awaitEvents(1)
//    }
//
//    fun awaitEvents(count: Int) {
//        Awaitility.await().atMost(count.toLong(), TimeUnit.SECONDS).until(Callable { events.isNotEmpty() })
//    }
//
//    fun areEventsInOrder(): Boolean {
//        val integerIds = events.map { it.id }.map(String::toInt)
//        return Ordering.natural<Int>().isOrdered(integerIds)
//    }
//
//    private fun onEvent(id: com.richodemus.chronicler.test.util.Event) {
//        events.add(id)
//    }
//}
