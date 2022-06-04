package pl.memexurer.gaming.identification;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.serialization.SerializableObject;

public final class PartySettings implements SerializableObject {

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
  public void write(ProtocolBuffer buffer) {
    buffer.writeInt(serialize());
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    var settings = buffer.readInt();
    this.chatEnabled = (settings & 1) == 1;
    this.isPrivate = (settings & 2) == 2;
  }
}
