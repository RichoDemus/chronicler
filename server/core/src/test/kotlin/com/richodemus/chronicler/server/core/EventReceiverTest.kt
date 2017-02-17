package com.richodemus.chronicler.server.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class EventReceiverTest {
    @Test
    fun `Add one million events using AnyVersion`() {
        val oneMillion = 1000000
        val target = EventReceiver()

        IntRange(1, oneMillion).forEach {
            target.consume(AnyVersion(), Event(it.toString()))
        }

        assertThat(target.events).hasSize(oneMillion)
        assertThat(target.events).isEqualTo(target.events.sortedBy { it.event.id.toInt() })
    }

    @Test
    fun `Add one million events using NumericalVersion`() {
        val oneMillion = 1000000
        val target = EventReceiver()

        IntRange(0, oneMillion - 1).forEach {
            target.consume(NumericalVersion(it.toLong()), Event(it.toString()))
        }

        assertThat(target.events).hasSize(oneMillion)
        assertThat(target.events).isEqualTo(target.events.sortedBy { it.event.id.toInt() })
    }

    @Test
    fun `Add one million events from multiple threads`() {
        val oneMillion = 1000000
        val target = EventReceiver()

        val testClient = TestClient(10, oneMillion, target)
        testClient.start()
        println("Running")
        testClient.await()
        println("Done, asserting")
        assertThat(target.events.size).isGreaterThanOrEqualTo(oneMillion)
        assertThat(target.events.map { it.version.version }).isEqualTo(target.events.map { it.version.version }.sortedBy { it })
        val arrayList = ArrayList(target.events)
        arrayList.forEachIndexed { i, event ->
            if(arrayList.last() == arrayList[i]){
                return@forEachIndexed
            }
            if (i.mod(1000) == 0) {
                println("Comparing event #$i ${arrayList[i]} to next one ${arrayList[i + 1]}")
            }
            val nextMinusOne = arrayList[i + 1].version.version - 1
            assertThat(event.version.version).withFailMessage("Comparing event #$i ${arrayList[i]} to next one ${arrayList[i + 1]}").isEqualTo(nextMinusOne)
        }
    }
}
