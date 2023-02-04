package pl.memexurer.gaming.cloudnet.packet;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufable;

public abstract class GameRequest implements DataBufable {
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
  public void writeData(DataBuf.Mutable dataBuf) {
    dataBuf.writeString(gameId);
  }

  @Override
  public void readData(DataBuf dataBuf) {
    this.gameId = dataBuf.readString();
  }
}
