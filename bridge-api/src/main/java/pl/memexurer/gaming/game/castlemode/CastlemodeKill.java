package pl.memexurer.gaming.game.castlemode;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufable;

import java.util.UUID;

public final class CastlemodeKill implements DataBufable {

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
  public void writeData(DataBuf.Mutable dataBuf) {
    dataBuf.writeObject(type)
            .writeUniqueId(victim)
            .writeUniqueId(killer);
  }

  @Override
  public void readData(DataBuf dataBuf) {
    this.type = dataBuf.readObject(CastlemodeKillType.class);
    this.victim = dataBuf.readUniqueId();
    this.killer = dataBuf.readUniqueId();
  }
}
