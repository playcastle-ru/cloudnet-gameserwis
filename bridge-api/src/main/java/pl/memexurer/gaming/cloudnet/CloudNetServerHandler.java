package pl.memexurer.gaming.cloudnet;

import eu.cloudnetservice.driver.channel.ChannelMessage;
import eu.cloudnetservice.driver.database.DatabaseProvider;
import eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent;
import eu.cloudnetservice.driver.network.buffer.DataBuf;
import pl.memexurer.gaming.cloudnet.packet.GameCreationRequest;
import pl.memexurer.gaming.cloudnet.packet.GameCreationResponse;
import pl.memexurer.gaming.cloudnet.packet.GameJoinRequest;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.server.ServerHandler;

public abstract class CloudNetServerHandler implements ServerHandler {

    private final GameDatabase gameDatabase;

    protected CloudNetServerHandler(String groupName, DatabaseProvider databaseProvider) {
        this.gameDatabase = GameDatabase.createGameDatabase(groupName, databaseProvider);
    }

    @Override
    public void updateGame(Game game) {
        gameDatabase.update(game);
    }

    @Override
    public void endGame(String game) {
        gameDatabase.delete(game);
    }

    protected void handleEvent(ChannelMessageReceiveEvent event) {
        if (event.channel().equals(CloudNetServerPool.GAME_CREATION_CHANNEL)) {
            var request = event.content().readObject(GameCreationRequest.class);

            GameCreationResponse response;
            try {
                var handleResp = handleCreateRequest(request).join();
                response = new GameCreationResponse(handleResp);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                response = new GameCreationResponse(throwable.toString());
            }

            try {
                event.queryResponse(
                        ChannelMessage.buildResponseFor(event.channelMessage())
                                .buffer(DataBuf.empty().writeObject(response)).build()
                );
            } catch (Throwable throwable) {
                System.out.println("Serialization error.");
                throwable.printStackTrace();
            }
        } else if (event.channel().equals(CloudNetServerPool.GAME_JOIN_CHANNEL)) {
            var request = event.content().readObject(GameJoinRequest.class);
            try {
                handleJoinRequest(request).join();
                event.queryResponse(
                        ChannelMessage.buildResponseFor(event.channelMessage()).build()
                );
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                event.queryResponse(
                        ChannelMessage.buildResponseFor(event.channelMessage())
                                .message(throwable.toString())
                                .build()
                );
            }
        }
    }
}
