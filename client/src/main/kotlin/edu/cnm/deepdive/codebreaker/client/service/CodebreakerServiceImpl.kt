package edu.cnm.deepdive.codebreaker.client.service

import edu.cnm.deepdive.codebreaker.client.dto.GameRequest
import edu.cnm.deepdive.codebreaker.client.dto.GameResponse
import edu.cnm.deepdive.codebreaker.client.dto.GuessRequest
import edu.cnm.deepdive.codebreaker.client.dto.GuessResponse
import edu.cnm.deepdive.codebreaker.client.web.CodebreakerApi
import okhttp3.OkHttpClient
import java.util.*
import java.util.concurrent.CompletableFuture

class CodebreakerServiceImpl : CodebreakerService {
    private val client: OkHttpClient
    private val api: CodebreakerApi

    init {
        TODO("Not yet implemented; implement to initialize client and api")
        val properties = loadProperties()
    }

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

    private fun loadProperties(): Properties {
        val properties = Properties()
        return CodebreakerServiceImpl::class.java
            .classLoader
            .getResourceAsStream(PROPERTIES_FILE)
            .use {
                properties.load(it)
                properties
            }
    }

}

private const val PROPERTIES_FILE = "web-service.properties"
private const val LOG_LEVEL_KEY = "logLevel"
private const val BASE_URL = "baseUrl"
