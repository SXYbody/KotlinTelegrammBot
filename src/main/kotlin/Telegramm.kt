package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main  (args: Array<String>){
    val botUrl = args[0]
    val urlGetMe =  "https://api.telegram.org/bot$botUrl/getMe"
    val urlGetUpdates = "https://api.telegram.org/bot$botUrl/getUpdates"

    val client = HttpClient.newBuilder().build()

    val request = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    println(response.body())
}