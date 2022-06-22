package pl.memexurer.gaming.game.generic;

import pl.memexurer.gaming.game.GameData;

public interface GenericGameData extends GameData {
  GenericGameState getGameState();

  int getPlayerCount();

  int getMaxPlayerCount();

  long getGameEnd();
}
