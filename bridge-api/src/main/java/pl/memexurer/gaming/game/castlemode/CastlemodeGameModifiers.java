package pl.memexurer.gaming.game.castlemode;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.serialization.SerializableObject;
import pl.memexurer.gaming.game.GameModifiers;

public final class CastlemodeGameModifiers implements GameModifiers, SerializableObject {

  private String mapName;

  public CastlemodeGameModifiers(String mapName) {
    this.mapName = mapName;
  }

  public CastlemodeGameModifiers() {
  }

  public String getMapName() {
    return mapName;
  }

  @Override
  public void write(ProtocolBuffer buffer) {
    buffer.writeString(mapName);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    this.mapName = buffer.readString();
  }
}
