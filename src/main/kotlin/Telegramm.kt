package org.example

import org.example.TelegramBotService.checkNextQuestionAndSend
import org.example.TelegramBotService.getUpdates
import org.example.TelegramBotService.sendMessage
import java.lang.Exception

const val CALLBACK_DATA_ANSWER_PREFIX = "answer_"
const val DATA_STATISTIC = "statistics_clicked"
const val DATA_LEARN_WORD = "learn_word_clicked"
const val DATA_MENU = "/start"

fun main(args: Array<String>) {
    val trainer = try {
        LearnWordsTrainer(MAX_CORRECT_COUNT, MAX_QUESTION_WORDS)
    } catch (e: Exception) {
        println("Неккоректный файл")
        return
    }

    val botToken = args[0]
    var updateId = 0

    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val updateIdRegex: Regex = "\"update_id\":(\\d+)".toRegex()
    val messageIdChatRegex: Regex = "\"chat\":\\{\"id\":(\\d+)".toRegex()
    val messageDataRegex: Regex = "\"data\":\"(.+?)\"".toRegex()

    while (true) {
        Thread.sleep(2000)
        val updates = getUpdates(botToken, updateId)
        println(updates)

        val matchResultText: MatchResult? = messageTextRegex.find(updates)
        val groupsText = matchResultText?.groups

        val text: String = groupsText?.get(1)?.value ?: continue

        val matchResultId: MatchResult? = updateIdRegex.find(updates)
        val groupsId = matchResultId?.groups

        val idText = groupsId?.get(1)?.value
        updateId = idText?.toInt()?.plus(1) ?: updateId

        val matchResultIdChat: MatchResult? = messageIdChatRegex.find(updates)
        val groupsIdChat = matchResultIdChat?.groups
        val chatId = groupsIdChat?.get(1)?.value ?: continue

        val matchResultData: MatchResult? = messageDataRegex.find(updates)
        val groupsData = matchResultData?.groups
        val dataText: String? = groupsData?.get(1)?.value

        when {
            text == DATA_MENU -> TelegramBotService.sendMenu(chatId, botToken)
            dataText == DATA_STATISTIC -> {
                sendMessage(
                    "Выученно ${trainer.getStatistics().learned} из ${trainer.getStatistics().total} " +
                            "| ${String.format("%.0f", trainer.getStatistics().percent)}%",
                    chatId,
                    botToken,
                )
            }

            dataText == DATA_LEARN_WORD -> {
                checkNextQuestionAndSend(trainer, chatId, botToken)
            }

            dataText?.startsWith(CALLBACK_DATA_ANSWER_PREFIX) == true -> {
                val answerId: String = dataText.substringAfter(CALLBACK_DATA_ANSWER_PREFIX)
                if (trainer.checkAnswer(answerId)) {
                    sendMessage(
                        "Правильно!",
                        chatId,
                        botToken,
                    )
                } else {
                    sendMessage(
                        "Неправильно! ${trainer.question?.correctAnswer?.original} " +
                                "– это ${trainer.question?.correctAnswer?.translate}",
                        chatId,
                        botToken,
                    )
                }
                checkNextQuestionAndSend(trainer, chatId, botToken)
            }
        }
    }
}
