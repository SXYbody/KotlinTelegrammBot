package org.example

import org.example.TelegramBotService.getUpdates
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object TelegramBotService {
    const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"

    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates = "$TELEGRAM_BASE_URL$botToken/getUpdates?offset=$updateId"

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(text: String, chatId: String, botToken: String) {

        val urlSendMessage =
            "$TELEGRAM_BASE_URL$botToken/sendMessage?chat_id=$chatId&text=${URLEncoder.encode(text, "UTF-8")}"

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}


fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0

    while (true) {
        Thread.sleep(2000)
        val updates = getUpdates(botToken, updateId)
        println(updates)

        val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
        val matchResultText: MatchResult? = messageTextRegex.find(updates)
        val groupsText = matchResultText?.groups

        val text: String = groupsText?.get(1)?.value ?: continue

        val updateIdRegex: Regex = "\"update_id\":(\\d+)".toRegex()
        val matchResultId: MatchResult? = updateIdRegex.find(updates)
        val groupsId = matchResultId?.groups

        val idText = groupsId?.get(1)?.value
        updateId = idText?.toInt()?.plus(1) ?: updateId

        val messageIdChatRegex: Regex = "\"id\":(\\d+)".toRegex()
        val matchResultIdChat: MatchResult? = messageIdChatRegex.find(updates)
        val groupsIdChat = matchResultIdChat?.groups
        val chatId = groupsIdChat?.get(1)?.value ?: continue

        if (text == "Hello") TelegramBotService.sendMessage(text, chatId, botToken)
    }
}
