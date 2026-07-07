package edu.cnm.deepdive.codebreaker.app.repository

import androidx.lifecycle.LiveData
import edu.cnm.deepdive.codebreaker.app.model.dao.CompleteGameDao
import edu.cnm.deepdive.codebreaker.app.model.dao.IncompleteGameDao
import edu.cnm.deepdive.codebreaker.app.model.entity.CompleteGame
import edu.cnm.deepdive.codebreaker.app.model.entity.IncompleteGame
import edu.cnm.deepdive.codebreaker.model.Game
import java.time.Duration
import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.future

class GameRepositoryImpl(
    private val incompleteGameDao: IncompleteGameDao,
    private val completeGameDao: CompleteGameDao,
) : GameRepository {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun save(game: Game): CompletableFuture<Game> {
        return scope.future {
            val guesses = game.guesses
            if (game.isSolved) {
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
                completeGameDao.insert(completeGame)
            } else {
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
            game
        }
    }

    override fun delete(game: IncompleteGame): CompletableFuture<Void?> {
        TODO("Not yet implemented")
    }

    override fun delete(games: Collection<IncompleteGame>): CompletableFuture<Void?> {
        TODO("Not yet implemented")
    }

    override fun deleteAllIncomplete(): CompletableFuture<Void?> {
        TODO("Not yet implemented")
    }

    override fun deleteAllComplete(): CompletableFuture<Void?> {
        TODO("Not yet implemented")
    }

    override fun getAll(): LiveData<List<IncompleteGame>> {
        TODO("Not yet implemented")
    }

    override fun get(
        codeLength: Int,
        poolSize: Int
    ): LiveData<List<CompleteGame>> {
        TODO("Not yet implemented")
    }
}