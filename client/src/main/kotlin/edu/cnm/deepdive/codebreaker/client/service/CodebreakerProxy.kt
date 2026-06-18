package edu.cnm.deepdive.codebreaker.client.service

import edu.cnm.deepdive.codebreaker.client.dto.GameRequest
import edu.cnm.deepdive.codebreaker.client.dto.GameResponse
import edu.cnm.deepdive.codebreaker.client.dto.GuessRequest
import edu.cnm.deepdive.codebreaker.client.dto.GuessResponse
import java.util.concurrent.CompletableFuture

/**
 * Acts as a proxy to the
 * [Codebreaker web service](https://ddc-java.services/codebreaker-solitaire).
 */
interface CodebreakerProxy {

    /**
     * Requests the web service to start a game with the configuration specified in [game].
     *
     * @return [CompletableFuture] which completes successfully if a 2xx response is received from
     * the web service, and exceptionally otherwise.
     */
    fun startGame(game: GameRequest): CompletableFuture<GameResponse>

    fun getGame(gameId: String): CompletableFuture<GameResponse>

    fun deleteGame(gameId: String): CompletableFuture<Void?>

    fun submitGuess(gameId: String, guess: GuessRequest): CompletableFuture<GuessResponse>

    fun getGuess(gameId: String, guessId: String): CompletableFuture<GuessResponse>

    fun shutdown()

    companion object {

        @JvmStatic
        val instance: CodebreakerProxy
            get() = CodebreakerProxyImpl

    }
}