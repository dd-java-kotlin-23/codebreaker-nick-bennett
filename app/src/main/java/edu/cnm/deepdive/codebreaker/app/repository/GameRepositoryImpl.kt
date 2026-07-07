package edu.cnm.deepdive.codebreaker.app.repository

import androidx.lifecycle.LiveData
import edu.cnm.deepdive.codebreaker.app.model.dao.CompleteGameDao
import edu.cnm.deepdive.codebreaker.app.model.dao.IncompleteGameDao
import edu.cnm.deepdive.codebreaker.app.model.entity.CompleteGame
import edu.cnm.deepdive.codebreaker.app.model.entity.IncompleteGame
import edu.cnm.deepdive.codebreaker.model.Game
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.Duration
import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.future

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val incompleteGameDao: IncompleteGameDao,
    private val completeGameDao: CompleteGameDao,
) : GameRepository {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun save(game: Game): CompletableFuture<Game> =
        scope.future {
            if (game.isSolved) {
                saveComplete(game)
            } else {
                saveIncomplete(game)
            }
            game
        }

    override fun delete(game: IncompleteGame): CompletableFuture<Void?> =
        scope.future {
            incompleteGameDao.delete(game)
            null
        }

    override fun delete(games: Collection<IncompleteGame>): CompletableFuture<Void?> =
        scope.future {
            incompleteGameDao.delete(games)
            null
        }

    override fun deleteAllIncomplete(): CompletableFuture<Void?> =
        scope.future {
            incompleteGameDao.deleteAll()
            null
        }

    override fun deleteAllComplete(): CompletableFuture<Void?> =
        scope.future {
            completeGameDao.deleteAll()
            null
        }

    override fun getAll(): LiveData<List<IncompleteGame>> =
        incompleteGameDao.selectAll()

    override fun get(codeLength: Int, poolSize: Int): LiveData<List<CompleteGame>> =
        completeGameDao.select(codeLength, poolSize)

    private suspend fun saveIncomplete(game: Game) {
        val guesses = game.guesses
        val incompleteGame = if (guesses.isEmpty()) {
            IncompleteGame(
                externalKey = game.id,
                codeLength = game.length,
                poolSize = game.pool.codePoints().count().toInt(),
                started = game.created,
            )
        } else {
            val lastGuess = guesses.last()
            IncompleteGame(
                externalKey = game.id,
                codeLength = game.length,
                poolSize = game.pool.codePoints().count().toInt(),
                started = game.created,
                updated = lastGuess.submitted,
                guessCount = guesses.size,
                exactMatches = lastGuess.exactMatches,
                nearMatches = lastGuess.nearMatches,
            )
        }
        incompleteGameDao.insert(incompleteGame)
    }

    private suspend fun saveComplete(game: Game) {
        val guesses = game.guesses()
        val lastGuess = guesses.last()
        val completeGame = CompleteGame(
            externalKey = game.id,
            codeLength = game.length,
            poolSize = game.pool.codePoints().count().toInt(),
            completed = lastGuess.submitted,
            elapsedTime = Duration.between(
                guesses.first().submitted,
                lastGuess.submitted
            ).toMillis(),
            guessCount = guesses.size,
        )
        incompleteGameDao.delete(game.id)
        completeGameDao.insert(completeGame)
    }

}