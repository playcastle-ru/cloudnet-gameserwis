package pl.memexurer.gaming.cloudnet.joiner;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.serialization.SerializableObject;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import java.util.UUID;
import pl.memexurer.gaming.game.Game;

public class PlayerGameJoinPacket implements SerializableObject {
  public static final String CHANNEL = "7yOPE5LvpkynNqCk7nLEwl";

  private UUID uuid;
  private String serverId;
  private String gameId;

  private PlayerGameJoinPacket(UUID uuid, String serverId, String gameId) {
    this.uuid = uuid;
    this.serverId = serverId;
    this.gameId = gameId;
  }

  public PlayerGameJoinPacket() {
  }

  public void send() { // przepraszam za ten kod - zmusili mnie ;c
    ChannelMessage.builder()
        .buffer(ProtocolBuffer.create()
            .writeObject(this))
        .channel(CHANNEL)
        .targetService(CloudNetDriver.getInstance().getServicesRegistry().getFirstService(
                IPlayerManager.class)
            .getOnlinePlayer(uuid)
            .getLoginService().getServiceId().getName())
        .build().send();
  }

  @Override
  public void write(ProtocolBuffer buffer) {
    buffer.writeUUID(uuid);
    buffer.writeString(serverId);
    buffer.writeString(gameId);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    this.uuid = buffer.readUUID();
    this.serverId = buffer.readString();
    this.gameId = buffer.readString();
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getServerId() {
    return serverId;
  }

  public String getGameId() {
    return gameId;
  }

  public static void join(UUID player, Game game) {
    new PlayerGameJoinPacket(player, game.parent(), game.id()).send();
  }
}
