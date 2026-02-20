package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"

fun main(args: Array<String>) {
    val botToken = args[0]
    val urlGetMe = "$TELEGRAM_BASE_URL$botToken/getMe"
    val urlGetUpdates = "$TELEGRAM_BASE_URL$botToken/getUpdates"

    val client = HttpClient.newBuilder().build()

    val request1 = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val response1 = client.send(request1, HttpResponse.BodyHandlers.ofString())
    println(response1.body())

    val request2 = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response2 = client.send(request2, HttpResponse.BodyHandlers.ofString())
    println(response2.body())
}