package pl.memexurer.gaming.cloudnet;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.database.Database;
import java.util.List;
import pl.memexurer.gaming.game.Game;
import pl.memexurer.gaming.game.GameData;
import pl.memexurer.gaming.game.GameModifiers;
import pl.memexurer.gaming.game.castlemode.CastlemodeGameData;
import pl.memexurer.gaming.game.generic.GenericGameModifiers;

public class GameDatabase {

  private final Database database;

  private GameDatabase(Database database) {
    this.database = database;
  }

  public static GameDatabase createGameDatabase(String groupName, CloudNetDriver driver) {
    return new GameDatabase(driver.getDatabaseProvider().getDatabase(groupName));
  }

  public void create(Game game) {
    if (!database.insert(game.id(), JsonDocument.newDocument(WrappedGame.fromGame(game)))) {
      throw new IllegalArgumentException("Could not insert game");
    }
  }

  public List<Game> getAllGames() {
    return database.entries().entrySet().stream()
        .map(
            doc -> WrappedGame.toGame(doc.getKey(), doc.getValue().toInstanceOf(WrappedGame.class)))
        .toList();
  }

  public Game getById(String id) {
    return WrappedGame.toGame(id, database.get(id).toInstanceOf(WrappedGame.class));
  }

  public void update(Game game) {
    if (!database.update(game.id(), JsonDocument.newDocument(WrappedGame.fromGame(game)))) {
      throw new IllegalArgumentException("Could not update game");
    }
  }

  public void delete(String id) {
    if (!database.delete(id)) {
      throw new IllegalArgumentException("Could not delete game");
    }
  }

  public void deleteParent(String parent) {
    database.filter(
            (id, document) -> document.toInstanceOf(WrappedGame.class).parent.equals(parent)).keySet()
        .forEach(database::delete); //may be unstable
  }

  public int count() {
    return (int) database.getDocumentsCount();
  }

  private static final class WrappedGame {

    private String parent;
    private CastlemodeGameData data;
    private GenericGameModifiers modifiers;

    private WrappedGame(String parent, GameData data, GameModifiers modifiers) {
      this.parent = parent;
      this.data = (CastlemodeGameData) data;
      this.modifiers = (GenericGameModifiers) modifiers;
    }

    public WrappedGame() {
    }

    public static WrappedGame fromGame(Game game) {
      return new WrappedGame(game.parent(), game.gameData(), game.modifiers());
    }

    public static Game toGame(String id, WrappedGame game) {
      return new Game(game.parent, id, game.data, game.modifiers);
    }

    public String parent() {
      return parent;
    }

    public GameData data() {
      return data;
    }

    public GameModifiers modifiers() {
      return modifiers;
    }
  }
}
