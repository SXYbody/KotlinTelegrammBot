package org.example

import java.io.File

const val NUMBERS_PERCENTAGE = 100
const val MAX_CORRECT_COUNT = 3

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

fun loadDictionary(): MutableList<Word> {
    val file = File("dictionary")
    file.createNewFile()

    val dictionaryList = mutableListOf<Word>()

    for (i in file.readLines()) {
        val splitWord = i.split("|")
        val word = Word(splitWord[0], splitWord[1])
        when {
            splitWord.size == 3 -> try {
                dictionaryList.add(word.copy(correctAnswersCount = splitWord.last().toInt()))
            } catch (e: NumberFormatException) {
                dictionaryList.add(word)
            }

            splitWord.size == 2 -> dictionaryList.add(word)
            splitWord.size < 2 -> continue
        }
    }
    return dictionaryList
}

fun getStatistics(): String {
    val dictionary = loadDictionary()
    val totalCount: Int = dictionary.size
    val learnedCount: Int = dictionary.filter { it.correctAnswersCount >= MAX_CORRECT_COUNT }.size
    val percent = (learnedCount.toDouble() / totalCount.toDouble()) * NUMBERS_PERCENTAGE
    return "Выучено: $learnedCount из $totalCount слов | ${String.format("%.0f", percent)}%"
}

fun startLearnWords() {
    val dictionary = loadDictionary()
    val notLearnedList = dictionary.filter { it.correctAnswersCount < MAX_CORRECT_COUNT }

    do {
        if (notLearnedList.size > 0) {
            val questionWords = notLearnedList.shuffled().take(4)

            val correctAnswer = questionWords.random()
            val learnWord = """
                
                ${correctAnswer.original}:
                1 - ${questionWords[0].translate}
                2 - ${questionWords[1].translate}
                3 - ${questionWords[2].translate}
                4 - ${questionWords[3].translate}
            """.trimIndent()

            println(learnWord)

            val userAnswer = readln()

        } else {
            println("Все слова уже выученны!")
            return
        }

    } while (true)
}

fun main() {
    val dictionary = loadDictionary()

    do {
        println("Меню: \n1 – Учить слова \n2 – Статистика \n0 – Выход")
        val userNumber = readln()
        when (userNumber) {
            "1" -> println(startLearnWords())
            "2" -> println(getStatistics())
            "0" -> break
            else -> println("Введите число 1, 2 или 0")
        }

    } while (true)
}