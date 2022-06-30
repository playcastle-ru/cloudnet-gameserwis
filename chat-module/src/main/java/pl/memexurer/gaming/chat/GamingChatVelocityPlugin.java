package pl.memexurer.gaming.chat;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import pl.memexurer.gaming.chat.commands.MsgCommand;
import pl.memexurer.gaming.chat.commands.ReplyCommand;
import pl.memexurer.gaming.chat.commands.SimpleCommandMeta;
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
    server.getCommandManager().register(new SimpleCommandMeta(this, "msg", "pm"), new MsgCommand(
        dataSource));
    server.getCommandManager().register(new SimpleCommandMeta(this, "reply", "r"), new ReplyCommand(
        dataSource));
  }

}
