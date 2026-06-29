package edu.cnm.deepdive.codebreaker.client.dto

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object OffsetDateTimeAdapter {

    @ToJson
    fun toJson(value: OffsetDateTime?): String? =
        value?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    @FromJson
    fun fromJson(value: String?): OffsetDateTime? =
        value?.let {
            OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }
}