package pl.memexurer.gaming.cloudnet;

import eu.cloudnetservice.driver.channel.ChannelMessage;
import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import pl.memexurer.gaming.cloudnet.packet.GameCreationRequest;
import pl.memexurer.gaming.cloudnet.packet.GameCreationResponse;
import pl.memexurer.gaming.cloudnet.packet.GameJoinRequest;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.game.GameModifiers;
import pl.memexurer.gaming.identification.UserIdentification;
import pl.memexurer.gaming.server.Server;

import java.util.concurrent.CompletableFuture;

public class ManagedServer implements Server {

    private final ServiceInfoSnapshot snapshot;

    public ManagedServer(ServiceInfoSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public String getId() {
        return snapshot.name();
    }

    @Override
    public CompletableFuture<Game> submitGameCreationRequest(String gameId,
                                                             GameModifiers gameModifiers) {
        return ChannelMessage.builder()
                .channel(CloudNetServerPool.GAME_CREATION_CHANNEL)
                .buffer(DataBuf.empty().writeObject(new GameCreationRequest(gameId, gameModifiers)))
                .targetService(getId())
                .build().sendSingleQueryAsync()
                .thenApply(x -> x.content().readObject(GameCreationResponse.class))
                .thenApply(x -> {
                    if (x.getError() != null) {
                        throw new IllegalArgumentException(x.getError());
                    } else {
                        return new Game(getId(), gameId, x.getValue(), gameModifiers);
                    }
                });
    }

    public static CompletableFuture<Void> createConnectionReques(String serverId, String gameId, UserIdentification identification) {
        return ChannelMessage.builder()
                .channel(CloudNetServerPool.GAME_JOIN_CHANNEL)
                .buffer(DataBuf.empty()
                        .writeObject(new GameJoinRequest(gameId, identification)))
                .targetService(serverId)
                .build().sendSingleQueryAsync() //todo this thing ignores errors? clodunet has weird api
                .thenAccept(v -> identification.joinServer(gameId));
    }

    @Override
    public CompletableFuture<Void> createConnectionRequest(String gameId,
                                                           UserIdentification identification) {
        return createConnectionReques(getId(), gameId, identification);
    }
}
