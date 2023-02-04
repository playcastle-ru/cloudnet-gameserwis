package pl.memexurer.gaming.identification;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufable;

public final class PartySettings implements DataBufable {

  private boolean chatEnabled;
  private boolean isPrivate;

  public PartySettings(boolean chatEnabled, boolean isPrivate) {
    this.chatEnabled = chatEnabled;
    this.isPrivate = isPrivate;
  }

  private PartySettings() {

  }

  public int serialize() {
    return (chatEnabled ? 1 : 0) |
        (isPrivate ? 1 : 0);
  }

  public boolean isChatEnabled() {
    return chatEnabled;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  @Override
  public void writeData(DataBuf.Mutable dataBuf) {
    dataBuf.writeInt(serialize());
  }

  @Override
  public void readData( DataBuf dataBuf) {
    var settings = dataBuf.readInt();
    this.chatEnabled = (settings & 1) == 1;
    this.isPrivate = (settings & 2) == 2;
  }
}
