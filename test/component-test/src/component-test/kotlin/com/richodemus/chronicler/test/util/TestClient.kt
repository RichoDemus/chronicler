package com.richodemus.chronicler.test.util

import com.richodemus.chronicler.server.api.model.Event
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig.encoderConfig
import io.restassured.http.ContentType

internal class TestClient(port: Int) {
    val baseUrl: String = "http://localhost:$port"

    fun getAllEvents(): List<Event> {
        val result = RestAssured
                .given()
                .`when`().get("$baseUrl/api/events")
                .then().extract().body().`as`(Events::class.java)
        return result.events
    }

    fun addEvent(event: Event): Int {
        return RestAssured
                .given().contentType("application/json").config(RestAssured.config()
                .encoderConfig(encoderConfig()
                        .encodeContentTypeAs("application/json", ContentType.TEXT)))
                .body(event)
                .`when`().post("$baseUrl/api/events")
                .then().extract().statusCode()
    }

    fun addEvent(event: Event, page: Int): Int {
        return RestAssured
                .given().contentType("application/json").config(RestAssured.config()
                .encoderConfig(encoderConfig()
                        .encodeContentTypeAs("application/json", ContentType.TEXT)))
                .body(event)
                .`when`().put("$baseUrl/api/events/$page")
                .then().extract().statusCode()
    }
}
