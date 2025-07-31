package org.example

import java.io.File

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

fun main() {
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

            splitWord.size < 2 -> continue
        }
    }
    dictionaryList.forEach { println(it) }
}