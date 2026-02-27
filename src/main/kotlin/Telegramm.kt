package org.example

import org.example.TelegramBotService.getUpdates

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

        val messageIdChatRegex: Regex = "\"chat\":\\{\"id\":(\\d+)".toRegex()
        val matchResultIdChat: MatchResult? = messageIdChatRegex.find(updates)
        val groupsIdChat = matchResultIdChat?.groups
        val chatId = groupsIdChat?.get(1)?.value ?: continue

        if (text == "Hello") TelegramBotService.sendMessage(text, chatId, botToken)
    }
}
