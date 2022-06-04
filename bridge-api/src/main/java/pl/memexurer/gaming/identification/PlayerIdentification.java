package pl.memexurer.gaming.identification;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public final class PlayerIdentification implements UserIdentification {

  private UUID user;

  public PlayerIdentification(UUID user) {
    this.user = user;
  }

  PlayerIdentification() {
  }

  @Override
  public Collection<UUID> getMembers() {
    return Collections.singletonList(user);
  }

  @Override
  public void write(ProtocolBuffer buffer) {
    buffer.writeBoolean(false);
    buffer.writeUUID(user);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    this.user = buffer.readUUID();
  }

  public UUID getUser() {
    return user;
  }
}