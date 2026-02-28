package org.example

import org.example.TelegramBotService.getUpdates
import java.lang.Exception

fun main(args: Array<String>) {
    val trainer = try {
        LearnWordsTrainer(MAX_CORRECT_COUNT, MAX_QUESTION_WORDS)
    } catch (e: Exception){
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
        val dataText: String = groupsData?.get(1)?.value ?: continue

        if (text == "Hello") TelegramBotService.sendMessage(text, chatId, botToken)

        if (text == "/start") TelegramBotService.sendMenu(chatId, botToken)

        if (dataText == "statistics_clicked") TelegramBotService.sendMessage(
            "Вы выучили 100% слов",
            chatId,
            botToken,
        )
    }
}
