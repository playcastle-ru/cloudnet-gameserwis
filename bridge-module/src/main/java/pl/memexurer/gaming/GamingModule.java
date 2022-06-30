package pl.memexurer.gaming;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStopEvent;
import de.dytanic.cloudnet.driver.module.ModuleLifeCycle;
import de.dytanic.cloudnet.driver.module.ModuleTask;
import de.dytanic.cloudnet.driver.module.driver.DriverModule;
import java.util.concurrent.TimeUnit;
import pl.memexurer.gaming.cloudnet.CloudNetServerPool;
import pl.memexurer.gaming.cloudnet.GameDatabase;
import pl.memexurer.gaming.game.generic.GenericGameData;
import pl.memexurer.gaming.game.generic.GenericGameModifiers;
import pl.memexurer.gaming.game.generic.GenericGameState;
import pl.memexurer.gaming.server.ServerPool;

public class GamingModule extends DriverModule {
  private GameDatabase gameDatabase;

  @ModuleTask(event = ModuleLifeCycle.STARTED)
  public void init() {
    this.gameDatabase = GameDatabase.createGameDatabase("Castlemode", getDriver());
    getDriver().getEventManager().registerListener(this);
    getDriver().getEventManager().registerListener(new PluginIncludeListener());
    getLogger().info("Gaming module has started!");

    ServerPool serverPool = new CloudNetServerPool(getDriver(), "Castlemode");
    getDriver().getTaskExecutor().scheduleAtFixedRate(() -> {
      if(gameDatabase.getAllGames().stream()
          .filter(game -> game.gameData() instanceof GenericGameData)
          .noneMatch(game -> ((GenericGameData) game.gameData()).getGameState() == GenericGameState.WAITING)) {
        serverPool.createGame(new GenericGameModifiers("nigger")).whenComplete(
            (game, throwable) -> {
              if(throwable != null) {
                getLogger().error("Blad podczas tworzenia giery", throwable);
              }
            });
      }
    }, 0, 10, TimeUnit.SECONDS);
  }

  @EventListener
  public void handleServiceStop(CloudServiceStopEvent event) {
    getLogger().info("Service " + event.getServiceInfo().getName() + " has stopped - deleting games...");
    gameDatabase.deleteParent(event.getServiceInfo().getName());
  }
}
