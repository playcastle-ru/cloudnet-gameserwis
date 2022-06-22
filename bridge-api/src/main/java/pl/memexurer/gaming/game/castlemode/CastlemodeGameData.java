package pl.memexurer.gaming.game.castlemode;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import pl.memexurer.gaming.game.generic.GenericGameData;
import pl.memexurer.gaming.game.generic.GenericGameState;

public class CastlemodeGameData implements GenericGameData {

  private Collection<UUID> team1, team2, spectators;
  private Collection<CastlemodeKill> killList;
  private GenericGameState gameState;
  private int flagCapturePoints;
  private long gameEnd;
  private int playerCount;
  private int maxPlayerCount;

  public CastlemodeGameData(Collection<UUID> team1, Collection<UUID> team2,
      Collection<UUID> spectators, List<CastlemodeKill> killList, GenericGameState gameState,
      int flagCapturePoints, long gameEnd, int playerCount, int maxPlayerCount) {
    this.team1 = team1;
    this.team2 = team2;
    this.spectators = spectators;
    this.killList = killList;
    this.gameState = gameState;
    this.flagCapturePoints = flagCapturePoints;
    this.gameEnd = gameEnd;
    this.playerCount = playerCount;
    this.maxPlayerCount = maxPlayerCount;
  }

  public CastlemodeGameData() {
    this(new HashSet<>(), new HashSet<>(), new HashSet<>(), new ArrayList<>(),
        GenericGameState.WAITING, 0, 0, 0, 0);
  }

  public Collection<UUID> getTeam1() {
    return Collections.unmodifiableCollection(team1);
  }

  public Collection<UUID> getTeam2() {
    return Collections.unmodifiableCollection(team2);
  }

  public Collection<UUID> getSpectators() {
    return Collections.unmodifiableCollection(spectators);
  }

  public Collection<CastlemodeKill> getKillList() {
    return Collections.unmodifiableCollection(killList);
  }

  public GenericGameState getGameState() {
    return gameState;
  }

  public int getFlagCapturePoints() {
    return flagCapturePoints;
  }

  public long getGameEnd() {
    return gameEnd;
  }

  public int getPlayerCount() {
    return playerCount;
  }

  public int getMaxPlayerCount() {
    return maxPlayerCount;
  }

  @Override
  public void write(ProtocolBuffer buffer) {
    buffer.writeUUIDCollection(team1)
        .writeUUIDCollection(team2)
        .writeUUIDCollection(spectators)
        .writeObjectCollection(killList)
        .writeEnumConstant(gameState)
        .writeInt(flagCapturePoints)
        .writeLong(gameEnd)
        .writeInt(playerCount)
        .writeInt(maxPlayerCount);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    this.team1 = buffer.readUUIDCollection();
    this.team2 = buffer.readUUIDCollection();
    this.spectators = buffer.readUUIDCollection();
    this.killList = buffer.readObjectCollection(CastlemodeKill.class);
    this.gameState = buffer.readEnumConstant(GenericGameState.class);
    this.flagCapturePoints = buffer.readInt();
    this.gameEnd = buffer.readLong();
    this.playerCount = buffer.readInt();
    this.maxPlayerCount = buffer.readInt();
  }
}
