package com.richodemus.chronicler.test.util

import com.fasterxml.jackson.annotation.JsonCreator
import com.richodemus.chronicler.server.api.model.Event

internal class Events @JsonCreator constructor(val events: List<Event>)
