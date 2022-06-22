package pl.memexurer.gaming;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStopEvent;
import de.dytanic.cloudnet.driver.module.ModuleLifeCycle;
import de.dytanic.cloudnet.driver.module.ModuleTask;
import de.dytanic.cloudnet.driver.module.driver.DriverModule;
import pl.memexurer.gaming.cloudnet.GameDatabase;

public class GamingModule extends DriverModule {
  private GameDatabase gameDatabase;

  @ModuleTask(event = ModuleLifeCycle.STARTED)
  public void init() {
    this.gameDatabase = GameDatabase.createGameDatabase("Castlemode", getDriver());
    getDriver().getEventManager().registerListener(this);
    getDriver().getEventManager().registerListener(new PluginIncludeListener());
    getLogger().info("Gaming module has started!");
  }

  @EventListener
  public void handleServiceStop(CloudServiceStopEvent event) {
    getLogger().info("Service " + event.getServiceInfo().getName() + " has stopped - deleting games...");
    gameDatabase.deleteParent(event.getServiceInfo().getName());
  }
}
