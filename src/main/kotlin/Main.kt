package org.example

import java.io.File

const val NUMBERS_PERCENTAGE = 100
const val MAX_CORRECT_COUNT = 3
const val MAX_QUESTION_WORDS = 4
const val FAULT_WORD_LIST = 1

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
            val questionWords = notLearnedList.shuffled().take(MAX_QUESTION_WORDS)
            val correctAnswer = questionWords.random()

            var learnWord = correctAnswer.original
            for (i in questionWords) learnWord += "\n${questionWords.indexOf(i) + FAULT_WORD_LIST} - ${i.translate}"

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