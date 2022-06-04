package pl.memexurer.gaming.cloudnet;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.ext.bridge.WrappedChannelMessageReceiveEvent;
import java.util.concurrent.TimeUnit;
import pl.memexurer.gaming.cloudnet.packet.GameCreationRequest;
import pl.memexurer.gaming.cloudnet.packet.GameCreationResponse;
import pl.memexurer.gaming.cloudnet.packet.GameJoinRequest;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.server.ServerHandler;

public abstract class CloudNetServerHandler implements ServerHandler {

  private final GameDatabase gameDatabase;

  protected CloudNetServerHandler(String groupName, CloudNetDriver driver) {
    this.gameDatabase = GameDatabase.createGameDatabase(groupName, driver);
  }

  @Override
  public void updateGame(Game game) {
    gameDatabase.update(game);
  }

  @Override
  public void endGame(String game) {
    gameDatabase.delete(game);
  }

  protected void handleEvent(WrappedChannelMessageReceiveEvent event) {
    if (event.getChannel().equals(CloudNetServerPool.GAME_CREATION_CHANNEL)) {
      var request = event.getChannelMessage().getBuffer().readObject(GameCreationRequest.class);

      GameCreationResponse response;
      try {
        var handleResp = handleCreateRequest(request).join();
        response = new GameCreationResponse(handleResp);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        response = new GameCreationResponse(throwable.toString());
      }

      try {
        event.setQueryResponse(
            ChannelMessage.buildResponseFor(event.getChannelMessage())
                .buffer(ProtocolBuffer.create().writeObject(response)).build()
        );
      } catch (Throwable throwable) {
        System.out.println("Serialization error.");
        throwable.printStackTrace();
      }
    } else if (event.getChannel().equals(CloudNetServerPool.GAME_JOIN_CHANNEL)) {
      var request = event.getChannelMessage().getBuffer().readObject(GameJoinRequest.class);
      try {
        handleJoinRequest(request).join();
        event.setQueryResponse(
            ChannelMessage.buildResponseFor(event.getChannelMessage()).build()
        );
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        event.setQueryResponse(
            ChannelMessage.buildResponseFor(event.getChannelMessage())
                .message(throwable.toString())
                .build()
        );
      }
    }
  }
}
