package edu.cnm.deepdive.codebreaker.cli.view

import edu.cnm.deepdive.codebreaker.model.Game
import java.io.PrintStream
import java.util.ResourceBundle

private const val POOL_LABEL_KEY = "pool_label"
private const val LENGTH_LABEL_KEY = "length_label"
private const val GUESS_PROMPT_KEY = "guess_prompt"
private const val INVALID_GUESS_MESSAGE_KEY = "invalid_guess_message"
private const val GUESS_NUM_HEADER_KEY = "guess_num_header"
private const val GUESS_TEXT_HEADER_KEY = "guess_text_header"
private const val EXACT_MATCHES_HEADER_KEY = "exact_matches_header"
private const val NEAR_MATCHES_HEADER_KEY = "near_matches_header"
private const val SUCCESS_MESSAGE_KEY = "success_message"

class GameView(
    private val output: PrintStream,
    private val bundle: ResourceBundle,
    private val game: Game
) {

    private val poolLabel = bundle.getString(POOL_LABEL_KEY)
    private val lengthLabel = bundle.getString(LENGTH_LABEL_KEY)
    private val guessPrompt = bundle.getString(GUESS_PROMPT_KEY)
    private val invalidGuessMessage = bundle.getString(INVALID_GUESS_MESSAGE_KEY)
    private val guessNumHeader = bundle.getString(GUESS_NUM_HEADER_KEY)
    private val guessTextHeader = bundle.getString(GUESS_TEXT_HEADER_KEY)
    private val exactMatchesHeader = bundle.getString(EXACT_MATCHES_HEADER_KEY)
    private val nearMatchesHeader = bundle.getString(NEAR_MATCHES_HEADER_KEY)
    private val successMessage = bundle.getString(SUCCESS_MESSAGE_KEY)

    fun emitGameConfiguration() {
        output.println(poolLabel.format(game.pool))
        output.println(lengthLabel.format(game.length))
    }

}