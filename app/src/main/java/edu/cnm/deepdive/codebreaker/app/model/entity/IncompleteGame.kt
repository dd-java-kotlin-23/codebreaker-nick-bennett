package edu.cnm.deepdive.codebreaker.app.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(
    tableName = "incomplete_game",
    indices = [
        Index(value = ["external_key"], unique = true),
        Index(value = ["updated"])
    ]
)
data class IncompleteGame(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "incomplete_game_id")
    val id: Long = 0,

    @ColumnInfo(name = "external_key")
    val externalKey: String = "",

    @ColumnInfo(name = "code_length")
    val codeLength: Int = 0,

    @ColumnInfo(name = "pool_size")
    val poolSize: Int = 0,

    val started: OffsetDateTime = OffsetDateTime.now(),

    var updated: OffsetDateTime = OffsetDateTime.now(),

    @ColumnInfo(name = "guess_count")
    var guessCount: Int = 0,

    @ColumnInfo(name = "exact_matches")
    var exactMatches: Int = 0,

    @ColumnInfo(name = "near_matches")
    var nearMatches: Int = 0,
)