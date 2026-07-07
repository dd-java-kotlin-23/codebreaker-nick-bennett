package edu.cnm.deepdive.codebreaker.app.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.cnm.deepdive.codebreaker.app.model.entity.IncompleteGame

@Dao
interface IncompleteGameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: IncompleteGame): Long

    @Update
    suspend fun update(game: IncompleteGame): Int

    @Delete
    suspend fun delete(game: IncompleteGame): Int

    @Delete
    suspend fun delete(games: Collection<IncompleteGame>): Int

    @Query("DELETE FROM incomplete_game")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM incomplete_game ORDER BY updated DESC")
    fun selectAll(): LiveData<List<IncompleteGame>>

    @Query("SELECT * FROM incomplete_game WHERE incomplete_game_id = :id")
    suspend fun select(id: Long): IncompleteGame?

    @Query("SELECT * FROM incomplete_game WHERE external_key = :externalKey")
    suspend fun select(externalKey: String): IncompleteGame?
}