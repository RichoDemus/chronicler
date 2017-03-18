package com.richodemus.chronicler.test.util

import io.restassured.RestAssured

internal class ChroniclerTestClient {
    val baseUrl: String

    constructor(port: Int) {
        baseUrl = "http://localhost:$port"
    }

    fun mainPage() = RestAssured
            .given()
            .`when`().get(baseUrl)
            .then().extract().statusCode()

    fun swaggerPage() = RestAssured
            .given()
            .`when`().get(baseUrl)
            .then().extract().body().asString()
}
