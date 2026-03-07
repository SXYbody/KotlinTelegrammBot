package org.example

import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object TelegramBotService {
    private const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"
    private val client = HttpClient.newBuilder().build()

    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates = "$TELEGRAM_BASE_URL$botToken/getUpdates?offset=$updateId"

        val request = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(text: String, chatId: String, botToken: String): String {

        val urlSendMessage =
            "$TELEGRAM_BASE_URL$botToken/sendMessage?chat_id=$chatId&text=${URLEncoder.encode(text, "UTF-8")}"

        val request = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun checkNextQuestionAndSend(trainer: LearnWordsTrainer): Question? {
        val question = trainer.nextQuestion() ?: return null
        return question
    }

    fun sendNextQuestionAndSend(question: Question, chatId: String, botToken: String): String {
        val urlSendMessage = "$TELEGRAM_BASE_URL$botToken/sendMessage"

        val keyboardButton: String = question.wordList.mapIndexed { index, word ->
            """
                [
                    {
                        "text": "${word.translate}",
                        "callback_data": "${CALLBACK_DATA_ANSWER_PREFIX}${index}"
                    }
                ]  
                """.trimIndent()
        }.joinToString(",")

        val sendLearnWordMenu = """
            {
                "chat_id": $chatId,
                "text": "Как переводиться слово: ${question.correctAnswer.original}",
                "reply_markup": {
                    "inline_keyboard": [                   
                         $keyboardButton                      
                    ]
                }
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendLearnWordMenu))
            .build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
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

        val request = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}