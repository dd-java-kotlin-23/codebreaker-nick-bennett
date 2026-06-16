package edu.cnm.deepdive.codebreaker.service;

import edu.cnm.deepdive.codebreaker.client.dto.GameRequest;
import edu.cnm.deepdive.codebreaker.client.dto.GameResponse;
import edu.cnm.deepdive.codebreaker.client.dto.GuessResponse;
import edu.cnm.deepdive.codebreaker.client.service.CodebreakerProxy;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

class CodebreakerServiceImpl implements CodebreakerService {

  private static final int MIN_LENGTH = 1;
  private static final int MAX_LENGTH = 20;

  private final CodebreakerProxy proxy = CodebreakerProxy.getInstance(); // FIXME: 6/15/26 Use dependency injection.

  @Override
  public CompletableFuture<Game> startGame(String pool, int length) {
    return isGameConfigurationValid(pool, length)
        ? proxy.startGame(new GameRequest(pool, length))
            .thenApply(CodebreakerServiceImpl::buildGame)
        : CompletableFuture.failedFuture(new IllegalArgumentException());
  }

  @Override
  public CompletableFuture<Game> getGame(String id) {
    return proxy.getGame(id).thenApply(CodebreakerServiceImpl::buildGame);
  }

  @Override
  public CompletableFuture<Void> deleteGame(String id) {
    return proxy.deleteGame(id);
  }

  @Override
  public CompletableFuture<Game> submitGuess(Game game, String text) {
    boolean isValidLength = (text.codePoints().count() == game.length());
    Set<Integer> poolSet = game
        .pool()
        .codePoints()
        .boxed()
        .collect(Collectors.toSet());
    boolean isValidContent = text
        .codePoints()
        .allMatch(poolSet::contains);
  }

  @Override
  public void shutdown() {
    // TODO: 6/15/26 Invoke as-yet-unimoplemented shutdown method in proxy.
  }

  private static boolean isGameConfigurationValid(String pool, int length) {
    boolean isValidPool = pool
        .codePoints()
        .distinct()
        .noneMatch(new IntPredicate() {
          @Override
          public boolean test(int codePoint) {
            return Character.isWhitespace(codePoint)
                || Character.isISOControl(codePoint)
                || !Character.isDefined(codePoint);
          }
        });
    boolean isValidLength = (length >= MIN_LENGTH && length <= MAX_LENGTH);
    return isValidPool && isValidLength;
  }

  private static Game buildGame(GameResponse response) {
    List<Guess> guesses = response
        .getGuesses()
        .stream()
        .map(CodebreakerServiceImpl::buildGuess)
        .collect(Collectors.toCollection(LinkedList::new));
    return new Game(response.getId(), response.getPool(), response.getLength(), guesses,
        response.getCreated());
  }

  private static Guess buildGuess(GuessResponse response) {
    return new Guess(response.getText(), response.getExactMatches(),
        response.getNearMatches(), response.getSolution(), response.getCreated());
  }

}
