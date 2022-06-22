package pl.memexurer.gaming.game.generic;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.serialization.SerializableObject;
import pl.memexurer.gaming.game.GameModifiers;

public final class GenericGameModifiers implements GameModifiers, SerializableObject {

  private String mapName;

  public GenericGameModifiers(String mapName) {
    this.mapName = mapName;
  }

  public GenericGameModifiers() {
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
