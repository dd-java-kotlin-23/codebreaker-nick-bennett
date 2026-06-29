package edu.cnm.deepdive.codebreaker.client.di

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Module
import dagger.Provides
import edu.cnm.deepdive.codebreaker.client.service.CodebreakerProxy
import edu.cnm.deepdive.codebreaker.client.service.CodebreakerProxyImpl
import edu.cnm.deepdive.codebreaker.client.web.CodebreakerApi
import jakarta.inject.Singleton
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
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

    internal const val PROPERTIES_FILE = "web-service.properties"
    internal const val LOG_LEVEL_KEY = "logLevel"
    internal const val BASE_URL_KEY = "baseUrl"

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

    internal fun loadProperties(): Properties {
        val properties = Properties()
        return CodebreakerProxyImpl::class.java
            .classLoader
            .getResourceAsStream(PROPERTIES_FILE)
            .use {
                properties.load(it)
                properties
            }
    }

    internal fun buildMoshi(): Moshi =
        Moshi.Builder()
            .add(OffsetDateTimeAdapter)
            .build()

    internal fun buildClient(properties: Properties): OkHttpClient {
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

    internal fun buildApi(properties: Properties, moshi: Moshi, client: OkHttpClient): CodebreakerApi =
        Retrofit.Builder()
            .baseUrl(properties[BASE_URL_KEY] as String)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(CodebreakerApi::class.java)

}

internal object OffsetDateTimeAdapter {

    @ToJson
    fun toJson(value: OffsetDateTime?): String? =
        value?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    @FromJson
    fun fromJson(value: String?): OffsetDateTime? =
        value?.let {
            OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }
}
