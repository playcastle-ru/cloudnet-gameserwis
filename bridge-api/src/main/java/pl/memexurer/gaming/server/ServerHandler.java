package pl.memexurer.gaming.server;

import java.util.concurrent.CompletableFuture;
import pl.memexurer.gaming.cloudnet.packet.GameCreationRequest;
import pl.memexurer.gaming.cloudnet.packet.GameJoinRequest;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.game.GameData;

public interface ServerHandler {
  void updateGame(Game game);

  void endGame(String id);

  CompletableFuture<Void> handleJoinRequest(GameJoinRequest request);

  CompletableFuture<GameData> handleCreateRequest(GameCreationRequest request);
}
