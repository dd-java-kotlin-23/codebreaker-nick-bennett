package edu.cnm.deepdive.codebreaker.app.repository

import androidx.lifecycle.LiveData
import edu.cnm.deepdive.codebreaker.app.model.entity.CompleteGame
import edu.cnm.deepdive.codebreaker.app.model.entity.IncompleteGame
import edu.cnm.deepdive.codebreaker.model.Game
import java.util.concurrent.CompletableFuture

interface GameRepository {

    fun save(game: Game): CompletableFuture<Game>

    fun delete(game: IncompleteGame): CompletableFuture<Void?>

    fun delete(games: Collection<IncompleteGame>): CompletableFuture<Void?>

    fun delete(externalKey: String): CompletableFuture<Void?>

    fun deleteAllIncomplete(): CompletableFuture<Void?>

    fun deleteAllComplete(): CompletableFuture<Void?>

    fun getAll(): LiveData<List<IncompleteGame>>

    fun get(codeLength: Int, poolSize: Int): LiveData<List<CompleteGame>>

}