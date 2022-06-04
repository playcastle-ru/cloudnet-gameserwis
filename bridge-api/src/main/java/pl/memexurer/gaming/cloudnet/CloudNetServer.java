package pl.memexurer.gaming.cloudnet;

import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import java.util.concurrent.CompletableFuture;
import pl.memexurer.gaming.cloudnet.packet.GameCreationRequest;
import pl.memexurer.gaming.cloudnet.packet.GameCreationResponse;
import pl.memexurer.gaming.cloudnet.packet.GameJoinRequest;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.game.GameModifiers;
import pl.memexurer.gaming.identification.UserIdentification;
import pl.memexurer.gaming.server.Server;

public class CloudNetServer implements Server {

  private final ServiceInfoSnapshot snapshot;

  public CloudNetServer(ServiceInfoSnapshot snapshot) {
    this.snapshot = snapshot;
  }

  @Override
  public String getId() {
    return snapshot.getName();
  }

  @Override
  public CompletableFuture<Game> submitGameCreationRequest(String gameId,
      GameModifiers gameModifiers) {
    var cf = new CompletableFuture<Game>();

    ChannelMessage.builder()
        .channel(CloudNetServerPool.GAME_CREATION_CHANNEL)
        .buffer(ProtocolBuffer.create().writeObject(new GameCreationRequest(gameId, gameModifiers)))
        .targetService(getId())
        .build().sendSingleQueryAsync()
        .map(x -> x.getBuffer().readObject(GameCreationResponse.class))
        .onComplete(x -> {
          if (x.getError() != null) {
            cf.completeExceptionally(new IllegalArgumentException(x.getError()));
          } else {
            cf.complete(new Game(getId(), gameId, x.getValue(), gameModifiers));
          }
        })
        .onFailure(CompletableFuture::failedFuture);
    return cf;
  }

  @Override
  public CompletableFuture<Void> createConnectionRequest(String gameId,
      UserIdentification userIdentification) {
    var cf = new CompletableFuture<Void>();

    ChannelMessage.builder()
        .channel(CloudNetServerPool.GAME_JOIN_CHANNEL)
        .buffer(ProtocolBuffer.create()
            .writeObject(new GameJoinRequest(gameId, userIdentification)))
        .targetService(getId())
        .build().sendSingleQueryAsync()
        .onComplete(x -> {
          if (x.getMessage() != null) {
            cf.completeExceptionally(new IllegalArgumentException(x.getMessage()));
          } else {
            cf.complete(null);
          }
        })
        .onFailure(CompletableFuture::failedFuture);
    return cf.thenAccept(v -> userIdentification.joinServer(getId()));
  }
}
