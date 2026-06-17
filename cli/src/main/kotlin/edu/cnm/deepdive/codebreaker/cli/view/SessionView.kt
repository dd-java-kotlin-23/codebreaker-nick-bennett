package edu.cnm.deepdive.codebreaker.cli.view

import java.io.PrintStream
import java.util.ResourceBundle

private const val PLAY_AGAIN_PROMPT_KEY = "play_again_prompt"

class SessionView(private val output: PrintStream, private val bundle: ResourceBundle) {

    private val playAgainPrompt: String

    init {
        playAgainPrompt = bundle.getString(PLAY_AGAIN_PROMPT_KEY)
    }

    fun emitPlayAgainPrompt() {
        output.print(playAgainPrompt)
    }

}