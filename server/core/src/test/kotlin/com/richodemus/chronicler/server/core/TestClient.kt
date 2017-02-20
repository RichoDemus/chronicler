package com.richodemus.chronicler.server.core

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class TestClient(val threads: Int, val events: Int, val receiver: EventReceiver) {
    val executorService: ExecutorService = Executors.newFixedThreadPool(threads)
    val counter = AtomicLong()

    fun start() {
        IntRange(1, threads).forEach {
            executorService.execute {
                while (true) {
                    val id = counter.andIncrement
                    if (id > events) {
                        println("${Thread.currentThread().name} done at event $id")
                        break
                    }
                    var sent = false
                    while (!sent) {
                        try {
                            receiver.consume(receiver.getVersion(), Event(id.toString()))
                            sent = true
                        } catch (e: IllegalStateException) {
                        }
                    }
                }
            }
        }
    }

    fun await() {
        executorService.shutdown()
        executorService.awaitTermination(1, TimeUnit.HOURS)
    }
}
