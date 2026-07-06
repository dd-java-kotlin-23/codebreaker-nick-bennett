package edu.cnm.deepdive.codebreaker.app.service.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import edu.cnm.deepdive.codebreaker.app.model.dao.CompleteGameDao
import edu.cnm.deepdive.codebreaker.app.model.dao.IncompleteGameDao
import edu.cnm.deepdive.codebreaker.app.model.entity.CompleteGame
import edu.cnm.deepdive.codebreaker.app.model.entity.IncompleteGame
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

private const val VERSION = 1

@Database(
    entities = [IncompleteGame::class, CompleteGame::class],
    version = VERSION,
)
@TypeConverters(CodebreakerDatabase::class)
abstract class CodebreakerDatabase : RoomDatabase() {

    abstract fun getIncompleteGameDao(): IncompleteGameDao

    abstract fun getCompleteGameDao(): CompleteGameDao

    companion object {
        @JvmStatic
        @TypeConverter
        fun toLong(value: OffsetDateTime?): Long? =
            value?.toEpochSecond()

        @JvmStatic
        @TypeConverter
        fun toOffsetDateTime(value: Long?): OffsetDateTime? =
            value?.let { OffsetDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC) }
    }
}