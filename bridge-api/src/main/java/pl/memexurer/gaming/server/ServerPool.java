package pl.memexurer.gaming.server;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.game.GameModifiers;
import pl.memexurer.gaming.identification.UserIdentification;

public interface ServerPool {
    //dla adminow
    Collection<? extends Server> getAvailableServers();

    int getAvailableServerCount();

    //gui inventory
    List<Game> getAvailableGames();

    //tworzenie gierki, dla adminow
    //automatyczne tworzenie gierki
    CompletableFuture<Game> createGame(GameModifiers modifiers);

    CompletableFuture<Void> createConnectionRequest(Game game, UserIdentification identification);

    //komenda /joinserver (serverid), dla adminow
    Optional<? extends Server> findServer(String id);

    /**
     * @param id full game id (serverid-gameid)
     */
    Optional<? extends Game> findGame(String id);
}
