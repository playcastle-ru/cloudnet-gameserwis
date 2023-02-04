package pl.memexurer.gaming.chat;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.provider.ServiceTaskProvider;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import pl.memexurer.gaming.chat.commands.MsgCommand;
import pl.memexurer.gaming.chat.commands.ReplyCommand;
import pl.memexurer.gaming.chat.commands.SimpleCommandMeta;
import pl.memexurer.gaming.chat.commands.SpectateCommand;
import pl.memexurer.jedisdatasource.api.JedisDataSource;
import pl.memexurer.jedisdatasource.api.JedisDataSourceProvider;

@Plugin(id = "gaming-chat", dependencies = {
    @Dependency(id = "jedis-data-source")
})
public class GamingChatVelocityPlugin {

  @Inject
  private ProxyServer server;

  private static JedisDataSource findDataSource(ProxyServer server) {
    for (var plugin : server.getPluginManager().getPlugins()) {
      var dataSource = plugin.getInstance()
          .filter(object -> object instanceof JedisDataSourceProvider)
          .map(object -> (JedisDataSourceProvider) object);
      if (dataSource.isPresent()) {
        return dataSource.get().getDataSource();
      }
    }
    return null;
  }

  @Subscribe
  public void onInit(ProxyInitializeEvent event) {
    var dataSource = findDataSource(server);
    var playerManager = InjectionLayer.ext().instance(ServiceRegistry.class)
            .firstProvider(PlayerManager.class);
    var serviceProvider = InjectionLayer.ext().instance(CloudServiceProvider.class);

    server.getCommandManager().register(new SimpleCommandMeta(this, "msg", "pm"), new MsgCommand(
        dataSource, playerManager));
    server.getCommandManager().register(new SimpleCommandMeta(this, "reply", "r"), new ReplyCommand(
        dataSource, playerManager));
    server.getCommandManager().register(new SimpleCommandMeta(this, "spect", "spectate"), new SpectateCommand(playerManager, serviceProvider));
  }

}
