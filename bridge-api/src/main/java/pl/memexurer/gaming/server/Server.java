package pl.memexurer.gaming.server;

import java.util.concurrent.CompletableFuture;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.game.GameModifiers;
import pl.memexurer.gaming.identification.UserIdentification;

public interface Server {
    String getId();

    CompletableFuture<Game> submitGameCreationRequest(String gameId, GameModifiers modifiers);

    CompletableFuture<Void> createConnectionRequest(String gameId, UserIdentification identification);
}
