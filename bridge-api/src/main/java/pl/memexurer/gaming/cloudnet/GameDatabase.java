package pl.memexurer.gaming.cloudnet;

import eu.cloudnetservice.common.document.gson.JsonDocument;
import eu.cloudnetservice.driver.database.Database;
import eu.cloudnetservice.driver.database.DatabaseProvider;
import pl.memexurer.gaming.game.Game;

import java.util.List;

public class GameDatabase {

    private final Database database;

    private GameDatabase(Database database) {
        this.database = database;
    }

    public static GameDatabase createGameDatabase(String groupName, DatabaseProvider databaseProvider) {
        return new GameDatabase(databaseProvider.database(groupName));
    }

    public void create(Game game) {
        if (!database.insert(game.id(), JsonDocument.newDocument(game))) {
            throw new IllegalArgumentException("Could not insert game");
        }
    }

    public List<Game> getAllGames() {
        return database.entries().values().stream()
                .map(strings -> strings.toInstanceOf(Game.class))
                .toList();
    }

    public Game getById(String id) {
        JsonDocument document = database.get(id);
        if (document == null)
            return null;

        return document.toInstanceOf(Game.class);
    }

    public void update(Game game) {
        database.insert(game.id(), JsonDocument.newDocument(game));
    }

    public void delete(String id) {
        if (!database.delete(id)) {
            throw new IllegalArgumentException("Could not delete game");
        }
    }

    public void deleteParent(String parent) {
        database.find("parent", parent)
                .forEach(json -> database.delete(json.getString("id"))); //may be unstable
    }

    public int count() {
        return (int) database.documentCount();
    }
}
