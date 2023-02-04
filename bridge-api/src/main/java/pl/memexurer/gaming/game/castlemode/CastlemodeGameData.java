package pl.memexurer.gaming.game.castlemode;

import java.util.*;

import com.google.gson.reflect.TypeToken;
import eu.cloudnetservice.driver.network.buffer.DataBuf;
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
  public void writeData(DataBuf.Mutable dataBuf) {
    dataBuf.writeObject(team1)
            .writeObject(team2)
            .writeObject(spectators)
            .writeObject(killList)
            .writeObject(gameState)
            .writeInt(flagCapturePoints)
            .writeLong(gameEnd)
            .writeInt(playerCount)
            .writeInt(maxPlayerCount);
  }

  @Override
  public void readData(DataBuf dataBuf) {
    this.team1 = dataBuf.readObject(new TypeToken<List<UUID>>(){}.getType());
    this.team2 = dataBuf.readObject(new TypeToken<List<UUID>>(){}.getType());
    this.spectators = dataBuf.readObject(new TypeToken<List<UUID>>(){}.getType());
    this.killList =  dataBuf.readObject(new TypeToken<List<CastlemodeKill>>(){}.getType());
    this.gameState = dataBuf.readObject(GenericGameState.class);
    this.flagCapturePoints = dataBuf.readInt();
    this.gameEnd = dataBuf.readLong();
    this.playerCount = dataBuf.readInt();
    this.maxPlayerCount = dataBuf.readInt();
  }
}
