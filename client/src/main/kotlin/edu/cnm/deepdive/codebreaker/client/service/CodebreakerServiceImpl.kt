package edu.cnm.deepdive.codebreaker.client.service

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.cnm.deepdive.codebreaker.client.dto.GameRequest
import edu.cnm.deepdive.codebreaker.client.dto.GameResponse
import edu.cnm.deepdive.codebreaker.client.dto.GuessRequest
import edu.cnm.deepdive.codebreaker.client.dto.GuessResponse
import edu.cnm.deepdive.codebreaker.client.web.CodebreakerApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.CompletableFuture

internal object CodebreakerServiceImpl : CodebreakerService {
    private val client: OkHttpClient
    private val api: CodebreakerApi

    init {
        val properties = loadProperties()
        client = buildClient(properties)
        api = buildApi(properties, client)
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

}

private class OffsetDateTimeAdapter {
    @ToJson
    fun toJson(value: OffsetDateTime) =
        value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    @FromJson
    fun fromJson(value: String) =
        OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

private const val PROPERTIES_FILE = "web-service.properties"
private const val LOG_LEVEL_KEY = "logLevel"
private const val BASE_URL_KEY = "baseUrl"

private fun loadProperties(): Properties =
    CodebreakerServiceImpl::class.java
        .classLoader
        .getResourceAsStream(PROPERTIES_FILE)
        .use {
            Properties().apply {
                load(it)
            }
        }

private fun buildMoshi(): Moshi =
    Moshi.Builder()
        .add(OffsetDateTimeAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

private fun buildClient(properties: Properties): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
        .setLevel(
            HttpLoggingInterceptor.Level.valueOf(
                properties.getProperty(LOG_LEVEL_KEY).uppercase()
            )
        )
    return OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor)
        .build()
}

private fun buildApi(properties: Properties, client: OkHttpClient): CodebreakerApi =
    Retrofit.Builder()
        .baseUrl(properties.getProperty(BASE_URL_KEY))
        .addConverterFactory(MoshiConverterFactory.create(buildMoshi()))
        .client(client)
        .build()
        .create(CodebreakerApi::class.java)

