package edu.cnm.deepdive.codebreaker.app.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.cnm.deepdive.codebreaker.app.model.entity.CompleteGame

private const val COMPLETE_GAME_QUERY = """
    SELECT
        *
    FROM 
        complete_game 
    WHERE 
        code_length = :codeLength 
        AND pool_size = :poolSize 
    ORDER BY 
        guess_count ASC, 
        elapsed_time ASC
"""

@Dao
interface CompleteGameDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: CompleteGame): Long
    
    @Query("DELETE FROM complete_game")
    suspend fun deleteAll(): Int
    
    @Query(COMPLETE_GAME_QUERY)
    fun select(codeLength: Int, poolSize: Int): LiveData<List<CompleteGame>>
}