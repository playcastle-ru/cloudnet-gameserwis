package pl.memexurer.gaming.cloudnet.packet;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import pl.memexurer.gaming.game.GameData;

public class GameCreationResponse extends GameResponse<GameData> {

  public GameCreationResponse(String error) {
    super(GameData.class, error);
  }

  public GameCreationResponse(GameData value) {
    super(GameData.class, value);
  }

  public GameCreationResponse() {
    super(GameData.class);
  }

  @Override
  public void write(ProtocolBuffer buffer) {
    buffer.writeBoolean(value != null);
    if (value != null) {
      buffer.writeString(value.getClass().getName());
      buffer.writeObject(value);
    } else {
      buffer.writeString(error);
    }
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    if (buffer.readBoolean()) {
      try {
        this.value = buffer.readObject(
            (Class<? extends GameData>) Class.forName(buffer.readString()));
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    } else {
      this.error = buffer.readString();
    }
  }
}
