package pl.memexurer.gaming.cloudnet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import eu.cloudnetservice.driver.database.DatabaseProvider;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.game.GameModifiers;
import pl.memexurer.gaming.identification.UserIdentification;
import pl.memexurer.gaming.server.Server;
import pl.memexurer.gaming.server.ServerPool;

public class CloudNetServerPool implements ServerPool {
  public static final String GAME_CREATION_CHANNEL = "game_creation";
  public static final String GAME_JOIN_CHANNEL = "game_join";

  private final GameDatabase gameDatabase;
  private final CloudServiceProvider serviceProvider;

  private final String groupName;

  public CloudNetServerPool(String groupName, DatabaseProvider databaseProvider, CloudServiceProvider serviceProvider) {
    this.serviceProvider = serviceProvider;
    this.gameDatabase = GameDatabase.createGameDatabase(groupName, databaseProvider);
    this.groupName = groupName;
  }

  private static String getGameId(Server server) {
    return padLeftZeros(
        Integer.toHexString(
            server.getId().hashCode() ^ ThreadLocalRandom
                .current().nextInt(65536)), 4);
  }

  private static String padLeftZeros(String inputString, int length) {
    if (inputString.length() >= length) {
      return inputString;
    }
    StringBuilder sb = new StringBuilder();
    while (sb.length() < length - inputString.length()) {
      sb.append('0');
    }
    sb.append(inputString);

    return sb.toString();
  }

  @Override
  public Collection<? extends Server> getAvailableServers() {
    return serviceProvider.servicesByGroup(groupName)
        .stream().map(ManagedServer::new).toList();
  }

  @Override
  public int getAvailableServerCount() {
    return gameDatabase.count();
  }

  @Override
  public List<Game> getAvailableGames() {
    return gameDatabase.getAllGames();
  }

  @Override
  public CompletableFuture<Game> createGame(GameModifiers modifiers) {
    return CompletableFuture.supplyAsync(() -> {
      for (var server : getAvailableServers()) {
        try {
          return server.submitGameCreationRequest(getGameId(server), modifiers)
              .orTimeout(5, TimeUnit.SECONDS)
              .thenApply(game -> {
                gameDatabase.create(game);
                return game;
              })
              .join();
        } catch (Throwable throwable) {
          throwable.printStackTrace();
        }
      }
      throw new CompletionException(
          new IllegalStateException("No servers found!")
      );
    });
  }

  @Override
  public CompletableFuture<Void> createConnectionRequest(Game game,
      UserIdentification identification) {
    return findServer(game.parent()).orElseThrow()
        .createConnectionRequest(game.id(), identification);
  }

  @Override
  public Optional<? extends Server> findServer(String id) {
    return Optional.of(new ManagedServer(serviceProvider.serviceByName(id)));
  }

  @Override
  public Optional<? extends Game> findGame(String id) {
    return Optional.ofNullable(gameDatabase.getById(id));
  }

  public GameDatabase getGameDatabase() {
    return gameDatabase;
  }
}
