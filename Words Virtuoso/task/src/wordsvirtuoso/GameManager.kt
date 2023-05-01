package wordsvirtuoso

import java.io.File
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.exitProcess

object GameManager {

    var invalidWords: Int = 0
    private var numberOfTurns = 0
    private var timeToResolve: Long = 0
    private var clueStrings = mutableListOf<String>()
    private var wrongChars = mutableSetOf<String>()

    fun programHasTwoArgs(args: Array<String>): Boolean {
        return args.count() == 2
    }

    fun fileExist(args: Array<String>, index: Int): Boolean {

        return File(args[index]).exists()
    }

    private fun hasFiveLetters(word: String): Boolean {
        return word.trim().count() == 5
    }

    private fun hasOnlyEnglishChars(word: String): Boolean {
        val englishCharsRegex: Regex = Regex("[A-Z|a-z]+")
        return word.matches(englishCharsRegex)
    }

    private fun hasNotDuplicateLetters(word: String): Boolean {
        return word.toSet().count() == 5
    }

    fun fileHasInvalidWords(file: File): Boolean {
        var hasInvalidWord = false
        file.readLines().forEach { word ->
            if (!hasFiveLetters(word) || !hasOnlyEnglishChars(word) || !hasNotDuplicateLetters(word)) {
                hasInvalidWord = true
                invalidWords++
            }
        }
        return hasInvalidWord
    }
    fun candidateWordsNotIncludedInWordsFile(args: Array<String>): Int {
        var wordsNotIncluded = 0
        val wordsFile = File(args[0])
        val candidatesFile = File(args[1])
        val wordsSet: MutableSet<String> = mutableSetOf()
        val candidatesSet: MutableSet<String> = mutableSetOf()
        wordsFile.readLines().forEach {
            wordsSet.add(it.lowercase())
        }
        candidatesFile.readLines().forEach {
            candidatesSet.add(it.lowercase())
        }
        for (candidate in candidatesSet) {
            if (!wordsSet.contains(candidate)) wordsNotIncluded++
        }
        return wordsNotIncluded
    }

    private fun getSecretWord(file: File): String {
        val words = file.readLines().toSet()
        return words.elementAt(Random.nextInt(0, words.count()))
    }

    fun startGame(args: Array<String>) {
        var gameFinished = false
        var inputValid = false
        var startTime: Long = 0
        println("Words Virtuoso")
        val secretWord = getSecretWord(File(args[1])).lowercase()
        while (!gameFinished) {
            numberOfTurns++
            if (numberOfTurns == 1) {
                startTime = System.currentTimeMillis()
            }
            println("Input a 5-letter word:")
            val playerChoice = readln().lowercase().trim()
            if (playerChoice == "exit") {
                gameFinished = true
                println("The game is over.")
                exitProcess(-1)
            } else {
                if (playerChoice == secretWord) {
                    val endTime = System.currentTimeMillis()
                    timeToResolve = endTime - startTime
                    if (clueStrings.isNotEmpty()) {
                        clueStrings.forEach { println(it) }
                    }
                    for (letter in secretWord) {
                        print("\u001B[48:5:10m${letter.uppercase()}\u001B[0m")
                    }
                    println()
                    println("Correct!")
                    if (numberOfTurns == 1) {
                        println("Amazing luck! The solution was found at once.")
                    } else {
                        println("The solution was found after $numberOfTurns tries in ${(timeToResolve.toDouble()/1000).roundToInt()} seconds.")
                    }
                    gameFinished = true
                    exitProcess(-1)
                } else {
                    if (!hasFiveLetters(playerChoice)) {
                        println("The input isn't a 5-letter word.")
                    } else if (playerChoice.any { !it.toString().matches(Regex("[a-z]")) }) {
                        println("One or more letters of the input aren't valid.")
                    } else if (!hasNotDuplicateLetters(playerChoice)) {
                        println("The input has duplicate letters.")
                    } else if (File(args[0]).readLines().none { it == playerChoice }) {
                        println("The input word isn't included in my words list.")
                    } else {
                        getClueWord(playerChoice, secretWord)
                    }
                }
            }
        }
    }

    private fun getClueWord(playerString: String, secretWord: String) {
        var clueWord = ""
        val secretWordLetters: MutableList<String> = mutableListOf()

        secretWord.forEach {
            secretWordLetters.add(it.toString())
        }
        for (i in 0 until playerString.count()) {
            when {
                playerString[i].toString() == secretWordLetters[i] -> {
                    clueWord += "\u001B[48:5:10m${playerString[i].uppercase()}\u001B[0m"
                }
                secretWordLetters.contains(playerString[i].toString()) -> {
                    clueWord += "\u001B[48:5:11m${playerString[i].uppercase()}\u001B[0m"
                }
                else -> {
                    wrongChars.add(playerString[i].uppercase())
                    clueWord += "\u001B[48:5:7m${playerString[i].uppercase()}\u001B[0m"
                }
            }
        }
        clueStrings.add(clueWord)
        clueStrings.forEach { println(it) }
        println("\u001B[48:5:14m${wrongChars.sorted().joinToString("").uppercase()}\u001B[0m")
    }
}