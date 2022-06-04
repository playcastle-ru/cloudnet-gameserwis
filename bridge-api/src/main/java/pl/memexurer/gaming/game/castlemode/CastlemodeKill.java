package pl.memexurer.gaming.game.castlemode;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.serialization.SerializableObject;
import java.util.UUID;

public final class CastlemodeKill implements SerializableObject {

  private UUID victim;
  private UUID killer;
  private CastlemodeKillType type;

  public CastlemodeKill(UUID victim, UUID killer,
      CastlemodeKillType type) {
    this.victim = victim;
    this.killer = killer;
    this.type = type;
  }

  public CastlemodeKill() {
  }

  public UUID getVictim() {
    return victim;
  }

  public UUID getKiller() {
    return killer;
  }

  public CastlemodeKillType getType() {
    return type;
  }


  @Override
  public void write(ProtocolBuffer buffer) {
    buffer.writeEnumConstant(type);
    buffer.writeUUID(victim);
    buffer.writeUUID(killer);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    this.type = buffer.readEnumConstant(CastlemodeKillType.class);
    this.victim = buffer.readUUID();
    this.killer = buffer.readUUID();
  }
}
