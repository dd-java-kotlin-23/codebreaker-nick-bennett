package edu.cnm.deepdive.codebreaker.client.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import edu.cnm.deepdive.codebreaker.client.dto.OffsetDateTimeAdapter
import edu.cnm.deepdive.codebreaker.client.service.CodebreakerProxy
import edu.cnm.deepdive.codebreaker.client.service.CodebreakerProxyImpl
import edu.cnm.deepdive.codebreaker.client.web.CodebreakerApi
import jakarta.inject.Singleton
import java.util.Properties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object ClientModule {

    private const val PROPERTIES_FILE = "web-service.properties"
    private const val LOG_LEVEL_KEY = "logLevel"
    private const val BASE_URL_KEY = "baseUrl"

    @Provides
    @Singleton
    @JvmStatic
    fun provideCodebreakerProxy(): CodebreakerProxy {
        val properties = loadProperties()
        val moshi = buildMoshi()
        val client = buildClient(properties)
        val api = buildApi(properties, moshi, client)
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        return CodebreakerProxyImpl(client, moshi, api, scope)
    }

    private fun loadProperties(): Properties {
        val properties = Properties()
        return CodebreakerProxyImpl::class.java
            .classLoader
            .getResourceAsStream(PROPERTIES_FILE)
            .use {
                properties.load(it)
                properties
            }
    }

    private fun buildMoshi(): Moshi =
        Moshi.Builder()
            .add(OffsetDateTimeAdapter)
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

    private fun buildApi(properties: Properties, moshi: Moshi, client: OkHttpClient): CodebreakerApi =
        Retrofit.Builder()
            .baseUrl(properties[BASE_URL_KEY] as String)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(CodebreakerApi::class.java)

}

