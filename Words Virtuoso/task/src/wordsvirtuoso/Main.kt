package wordsvirtuoso

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var secretWord: String = ""

    // Point 1
    if (!GameManager.programHasTwoArgs(args)) {
        println("Error: Wrong number of arguments.")
        exitProcess(-1)
    }
    // Point 2
    else if (!GameManager.fileExist(args, 0)) {
        println("Error: The words file ${args[0]} doesn't exist.")
        exitProcess(-1)
    }
    // Point 3
    else if (!GameManager.fileExist(args,1)) {
        println("Error: The candidate words file ${args[1]} doesn't exist.")
        exitProcess(-1)
    }
    // Point 4
    else if (GameManager.fileHasInvalidWords(File(args[0]))) {
        println("Error: ${GameManager.invalidWords} invalid words were found in the ${args[0]} file.")
        GameManager.invalidWords = 0
        exitProcess(-1)
    }
    // Point 5
    else if (GameManager.fileHasInvalidWords(File(args[1]))) {
        println("Error: ${GameManager.invalidWords} invalid words were found in the ${args[1]} file.")
        GameManager.invalidWords = 0
        exitProcess(-1)
    }
    // Point 6
    else if (GameManager.candidateWordsNotIncludedInWordsFile(args) > 0) {
        println("Error: ${GameManager.candidateWordsNotIncludedInWordsFile(args)} candidate words are not included in the ${args[0]} file.")
        exitProcess(-1)
    }
    // Point 7
    else {
        GameManager.startGame(args)
    }
}

