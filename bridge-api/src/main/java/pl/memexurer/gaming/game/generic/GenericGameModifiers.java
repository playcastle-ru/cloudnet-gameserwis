package pl.memexurer.gaming.game.generic;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufable;
import pl.memexurer.gaming.game.GameModifiers;

public final class GenericGameModifiers implements GameModifiers, DataBufable {

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
  public void writeData(DataBuf.Mutable dataBuf) {
    dataBuf.writeString(mapName);
  }

  @Override
  public void readData(DataBuf dataBuf) {
    this.mapName = dataBuf.readString();
  }
}
