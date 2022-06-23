package pl.memexurer.gaming.chat;

import de.dytanic.cloudnet.driver.module.ModuleLifeCycle;
import de.dytanic.cloudnet.driver.module.ModuleTask;
import de.dytanic.cloudnet.driver.module.driver.DriverModule;

public class GamingChatModule extends DriverModule {

  @ModuleTask(event = ModuleLifeCycle.STARTED)
  public void init() {
    getDriver().getEventManager().registerListener(new PluginIncludeListener());
    getLogger().info("Gaming chat module has started!");
  }
}
