package com.richodemus.chronicler.server.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class ChronicleTest {
    @Test
    fun `Add one million events using AnyVersion`() {
        val oneMillion = 1000000
        val target = Chronicle()

        IntRange(1, oneMillion).forEach {
            target.addEvent(AnyVersion(), Event(it.toString()))
        }

        assertThat(target.getEvents()).hasSize(oneMillion)
        assertThat(target.getEvents()).isEqualTo(target.getEvents().sortedBy { it.event.id.toInt() })
    }

    @Test
    fun `Add one million events using NumericalVersion`() {
        val oneMillion = 1000000
        val target = Chronicle()

        IntRange(0, oneMillion - 1).forEach {
            target.addEvent(NumericalVersion(it.toLong()), Event(it.toString()))
        }

        assertThat(target.getEvents()).hasSize(oneMillion)
        assertThat(target.getEvents()).isEqualTo(target.getEvents().sortedBy { it.event.id.toInt() })
    }

    @Test
    fun `Add one million events from multiple threads`() {
        val oneMillion = 1000000
        val target = Chronicle()

        val testClient = TestClient(10, oneMillion, target)
        testClient.start()
        println("Running")
        testClient.await()
        println("Done, asserting")
        assertThat(target.getEvents().size).isGreaterThanOrEqualTo(oneMillion)
        assertThat(target.getEvents().map { it.version.version }).isEqualTo(target.getEvents().map { it.version.version }.sortedBy { it })
        val arrayList = ArrayList(target.getEvents())
        arrayList.forEachIndexed { i, event ->
            if (arrayList.last() == arrayList[i]) {
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
