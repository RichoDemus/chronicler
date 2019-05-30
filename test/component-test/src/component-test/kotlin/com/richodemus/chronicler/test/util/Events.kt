package com.richodemus.chronicler.test.util

import com.fasterxml.jackson.annotation.JsonCreator

internal class Events @JsonCreator constructor(val events: List<Event>)
