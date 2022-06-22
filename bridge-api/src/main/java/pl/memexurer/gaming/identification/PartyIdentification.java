package pl.memexurer.gaming.identification;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import java.util.Collection;
import java.util.UUID;

public final class PartyIdentification implements UserIdentification {

  private UUID owner;
  private Collection<UUID> members;

  PartyIdentification() {
  }

  public PartyIdentification(UUID owner, Collection<UUID> members) {
    this.owner = owner;
    this.members = members;
  }

  @Override
  public Collection<UUID> getMembers() {
    return members;
  }

  public UUID getOwner() {
    return owner;
  }

  @Override
  public void write(ProtocolBuffer buffer) {
    buffer.writeBoolean(true);
    buffer.writeUUID(owner);
    buffer.writeUUIDCollection(members);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    this.owner = buffer.readUUID();
    this.members = buffer.readUUIDCollection();
  }
}