package org.example

import java.io.File

data class Statistic(
    val total: Int,
    val learned: Int,
    val percent: Double,
)

data class Question(
    val wordList: List<Word>,
    val correctAnswer: Word,
)


class LearnWordsTrainer(
    val maxCorrectCount: Int,
    val maxQuestionWords: Int,
) {

    val dictionary = loadDictionary()
    var question: Question? = null

    fun getStatistics(): Statistic {
        val totalCount: Int = dictionary.size
        val learnedCount: Int = dictionary.filter { it.correctAnswersCount >= maxCorrectCount }.size
        val percent = (learnedCount.toDouble() / totalCount.toDouble()) * NUMBERS_PERCENTAGE
        return Statistic(totalCount, learnedCount, percent)
    }

    fun nextQuestion(): Question? {
        var notLearnedList = dictionary.filter { it.correctAnswersCount < maxCorrectCount }
        if (notLearnedList.size <= maxQuestionWords) {
            val learnedList = dictionary.filter { it.correctAnswersCount >= maxCorrectCount }.shuffled().take(maxQuestionWords)
            notLearnedList = notLearnedList + learnedList
        }

        if (notLearnedList.isEmpty()) return null
        val questionWords = notLearnedList.shuffled().take(maxQuestionWords)
        val correctAnswer = questionWords.random()

        question = Question(wordList = questionWords, correctAnswer = correctAnswer)
        return question
    }

    fun checkAnswer(answer: String): Boolean {
        if (answer.toInt().minus(1) == question?.wordList?.indexOf(question?.correctAnswer)) {
            question?.correctAnswer?.correctAnswersCount++
            this.saveDictionary(this.dictionary)
            return true
        } else {
            return false
        }
    }

    fun loadDictionary(): List<Word> {
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

    fun saveDictionary(dictionary: List<Word>) {
        val file = File("dictionary")
        file.delete()
        file.createNewFile()

        dictionary.forEach {
            file.appendText(text = "${it.original}|${it.translate}|${it.correctAnswersCount}\n")
        }
    }
}
