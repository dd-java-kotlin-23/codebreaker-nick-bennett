package edu.cnm.deepdive.codebreaker.app.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(
    tableName = "complete_game",
    indices = [
        Index(
            value = ["code_length", "pool_size", "guess_count", "elapsed_time"],
            orders = [Index.Order.ASC, Index.Order.ASC, Index.Order.ASC, Index.Order.ASC],
        )
    ]
)
data class CompleteGame(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "complete_game_id")
    val id: Long = 0,

    @ColumnInfo(name = "code_length")
    val codeLength: Int = 0,

    @ColumnInfo(name = "pool_size")
    val poolSize: Int = 0,

    val completed: OffsetDateTime = OffsetDateTime.now(),

    @ColumnInfo(name = "elapsed_time")
    val elapsedTime: Long = 0,

    @ColumnInfo(name = "guess_count")
    val guessCount: Int = 0,
)
