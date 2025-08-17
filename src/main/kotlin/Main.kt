package org.example

import java.lang.Exception

const val NUMBERS_PERCENTAGE = 100
const val MAX_CORRECT_COUNT = 3
const val MAX_QUESTION_WORDS = 4
const val FAULT_WORD_LIST = 1

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

fun Question.printQuestionString(): String {
    var learnWord = this.correctAnswer.original
    for (i in this.wordList) learnWord += "\n${this.wordList.indexOf(i) + FAULT_WORD_LIST} - ${i.translate}"
    learnWord += "\n---------- \n0 - Меню"
    return learnWord
}

fun startLearnWords() {
    val trainer = try {
        LearnWordsTrainer(MAX_CORRECT_COUNT, MAX_QUESTION_WORDS)
    } catch (e: Exception){
        println("Неккоректный файл")
        return
    }

    do {
        val question = trainer.nextQuestion()

        if (question != null) {
            println(question.printQuestionString())

            val userAnswerInPut = readln()
            try {
                val checkAnswer = trainer.checkAnswer(userAnswerInPut)
                when {
                    userAnswerInPut.toInt() == 0 -> break
                    checkAnswer -> println("Правильно!")
                    !checkAnswer -> println("Неправильно! ${question.correctAnswer.original} – это ${question.correctAnswer.translate}")
                }
            } catch (e: NumberFormatException) {
                println("Неправильно! ${question.correctAnswer.original} – это ${question.correctAnswer.translate}")
                continue
            }

        } else {
            println("Все слова уже выученны!")
            return
        }
    } while (true)
}

fun main() {
    val trainer = try {
        LearnWordsTrainer(MAX_CORRECT_COUNT, MAX_QUESTION_WORDS)
    } catch (e: Exception){
        println("Неккоректный файл")
        return
    }

    do {
        println("Меню: \n1 – Учить слова \n2 – Статистика \n0 – Выход")
        val userNumber = readln()
        when (userNumber) {
            "1" -> println(startLearnWords())
            "2" -> println(
                "Выученно ${trainer.getStatistics().learned} из ${trainer.getStatistics().total} " +
                        "| ${String.format("%.0f", trainer.getStatistics().percent)}%"
            )

            "0" -> break
            else -> println("Введите число 1, 2 или 0")
        }

    } while (true)
}