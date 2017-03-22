package com.richodemus.chronicler.server.core

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test

internal class ChronicleTest {
    private val id = "uuid"
    private val data = "interesting data"

    private var eventListenerMock: EventCreationListener? = null
    private var eventPersisterMock: EventPersister? = null

    private var target: Chronicle? = null

    @Before
    fun setUp() {
        eventListenerMock = mock<EventCreationListener> {}
        eventPersisterMock = mock<EventPersister> {
            on { readEvents() } doReturn listOf<Event>().iterator()
        }
        target = Chronicle(eventListenerMock(), eventPersisterMock())
    }

    private fun eventListenerMock() = eventListenerMock!!
    private fun eventPersisterMock() = eventPersisterMock!!
    private fun target() = target!!

    @Test
    fun `New Chronicle should be empty`() {
        val result = target().getEvents()
        assertThat(result).hasSize(0)
    }

    @Test
    fun `New Chronicle should be at page zero`() {
        val result = target().page
        assertThat(result).isEqualTo(0L)
    }

    @Test
    fun `A Chronicle with one event should return one event`() {
        val target = target()
        target.addEvent(Event(id, 1L, ""))

        assertThat(target.getEvents()).hasSize(1)
    }

    @Test
    fun `Should send newly created event to listener`() {
        val target = target()
        val event = Event(id, 1L, "")
        target.addEvent(event)

        verify(eventListenerMock()).onEvent(eq(event))
    }

    @Test
    fun `Should send newly created event to persister`() {
        val target = target()
        val event = Event(id, 1L, "")
        target.addEvent(event)

        verify(eventPersisterMock()).persist(eq(event))
    }

    @Test
    fun `Should read events from persistence`() {
        val events = listOf(Event("one", 1L, ""), Event("two", 2L, ""))
        whenever(eventPersisterMock().readEvents()).thenReturn(events.iterator())
        target = Chronicle(eventListenerMock(), eventPersisterMock())

        assertThat(target().getEvents()).containsExactly(*events.toTypedArray())
    }

    @Test
    fun `Should read event ids from persistence`() {
        val events = listOf(Event("one", 1L, ""), Event("two", 2L, ""))
        whenever(eventPersisterMock().readEvents()).thenReturn(events.iterator())
        target = Chronicle(eventListenerMock(), eventPersisterMock())

        assertThat(target().getIds()).containsExactly(*events.map { it.id }.toTypedArray())
    }

    @Test
    fun `A Chronicle with one event should be at page one`() {
        val target = target()
        target.addEvent(Event(id, 1L, ""))

        assertThat(target.page).isEqualTo(1L)
    }

    @Test
    fun `Add pageless Event to empty Chronicle`() {
        val target = target()

        target.addEvent(Event(id, null, data))

        val result = target.getEvents().single()
        assertThat(result.id).isEqualTo(id)
        assertThat(result.page).isEqualTo(1L)
        assertThat(result.data).isEqualTo(data)
    }

    @Test
    fun `Add Event to first page of empty Chronicle`() {
        val target = target()

        target.addEvent(Event(id, 1L, data))

        val result = target.getEvents().single()
        assertThat(result.id).isEqualTo(id)
        assertThat(result.page).isEqualTo(1L)
        assertThat(result.data).isEqualTo(data)
    }

    @Test
    fun `Adding event at the wrong page should throw exception`() {
        val target = target()
        assertThatThrownBy { target.addEvent(Event(id, 2L, "")) }.isInstanceOf(WrongPageException::class.java)
    }

    @Test
    fun `Should not insert duplicate Event`() {
        val target = target()

        target.addEvent(Event(id, null, "original data"))
        target.addEvent(Event(id, null, "second data"))

        val result = target.getEvents()

        assertThat(result).hasSize(1)
        assertThat(result.single().data).isEqualTo("original data")
    }

    @Test
    fun `Should not send duplicate event to listener`() {
        val target = target()

        target.addEvent(Event(id, null, "original data"))
        target.addEvent(Event(id, null, "second data"))

        verify(eventListenerMock(), times(1)).onEvent(any())
    }
}
