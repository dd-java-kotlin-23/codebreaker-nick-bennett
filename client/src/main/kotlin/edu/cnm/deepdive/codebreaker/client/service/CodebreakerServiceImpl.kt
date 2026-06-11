package edu.cnm.deepdive.codebreaker.client.service

import edu.cnm.deepdive.codebreaker.dto.GameRequest
import edu.cnm.deepdive.codebreaker.dto.GameResponse
import edu.cnm.deepdive.codebreaker.dto.GuessRequest
import edu.cnm.deepdive.codebreaker.dto.GuessResponse
import java.util.concurrent.CompletableFuture

class CodebreakerServiceImpl : CodebreakerService {
    override fun startGame(game: GameRequest): CompletableFuture<GameResponse> {
        TODO("Not yet implemented")
    }

    override fun getGame(gameId: String): CompletableFuture<GameResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteGame(gameId: String): CompletableFuture<Void?> {
        TODO("Not yet implemented")
    }

    override fun submitGuess(
        gameId: String,
        guess: GuessRequest
    ): CompletableFuture<GuessResponse> {
        TODO("Not yet implemented")
    }

    override fun getGuess(
        gameId: String,
        guessId: String
    ): CompletableFuture<GuessResponse> {
        TODO("Not yet implemented")
    }
}