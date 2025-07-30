package org.example

import java.io.File

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: String? = "0",
)

fun main() {
    val file = File("dictionary")
    file.createNewFile()

    val dictionaryList = mutableListOf<Word>()

    for (i in file.readLines()) {
        val splitWord = i.split("|")
        val word = Word(splitWord[0], splitWord[1])
        if (splitWord.getOrNull(2) != null) dictionaryList.add(word.copy(correctAnswersCount = splitWord[2])) else dictionaryList.add(
            word
        )
    }
    dictionaryList.forEach { println(it) }
}