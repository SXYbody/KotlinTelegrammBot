package org.example

import java.io.File

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

fun main() {
    val dictionary = loadDictionary()

    do {
        println("Меню: \n1 – Учить слова \n2 – Статистика \n0 – Выход")
        val userNumber = readln()
        when (userNumber) {
            "1" -> println("Вы выбрали учить слова.")
            "2" -> println("Вы выбрали посмотреть статистику")
            "0" -> break
            else -> println("Введите число 1, 2 или 0")
        }

    } while (true)
}