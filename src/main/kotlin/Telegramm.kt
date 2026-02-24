package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0

    while (true) {
        Thread.sleep(2000)
        val updates = getUpdates(botToken, updateId)
        println(updates)

        val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
        val matchResult: MatchResult? = messageTextRegex.find(updates)
        val groups = matchResult?.groups

        val text = groups?.get(1)?.value
        println(text)

        val updateIdRegex: Regex = "\"update_id\":(\\d+)".toRegex()
        val matchResultId: MatchResult? = updateIdRegex.find(updates)
        val groupsId = matchResultId?.groups

        val idText = groupsId?.get(1)?.value
        updateId = idText?.toInt()?.plus(1) ?: updateId

        println(updateId)
    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "$TELEGRAM_BASE_URL$botToken/getUpdates?offset=$updateId"

    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}
