package org.example

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

    fun sendMenu(chatId: String, botToken: String): String? {
        val urlSendMessage = "$TELEGRAM_BASE_URL$botToken/sendMessage"

        val sendMenuBody = """
            {
                "chat_id": $chatId,
                "text": "Основное меню",
                "reply_markup": {
                    "inline_keyboard": [
                        [
                            {
                                "text": "Изучить слова",
                                "callback_data": "learn_word_clicked"
                            },
                            {
                                "text": "Статистика",
                                "callback_data": "statistics_clicked"
                            }

                        ]
                    ]
                }
            }
        """.trimIndent()

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}