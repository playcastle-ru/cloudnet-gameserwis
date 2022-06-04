package pl.memexurer.gaming.cloudnet.packet;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.serialization.SerializableObject;

public abstract class GameRequest implements SerializableObject {
  private String gameId;

  public GameRequest() {
  }

  protected GameRequest(String gameId) {
    this.gameId = gameId;
  }

  public String getGameId() {
    return gameId;
  }

  @Override
  public void write(ProtocolBuffer buffer) {
    buffer.writeString(gameId);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    this.gameId = buffer.readString();
  }
}
