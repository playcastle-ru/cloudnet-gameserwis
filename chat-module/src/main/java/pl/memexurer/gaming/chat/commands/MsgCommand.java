package pl.memexurer.gaming.chat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import java.util.Arrays;
import java.util.stream.Collectors;

import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import net.kyori.adventure.text.Component;
import pl.memexurer.jedisdatasource.api.JedisDataSource;
import redis.clients.jedis.params.SetParams;

public class MsgCommand implements SimpleCommand {

  private final JedisDataSource dataSource;
  private final PlayerManager playerManager;

  public MsgCommand(JedisDataSource dataSource,
                    PlayerManager playerManager) {
    this.dataSource = dataSource;
    this.playerManager = playerManager;
  }

  @Override
  public void execute(Invocation invocation) {
    if (!(invocation.source() instanceof Player player)) {
      invocation.source().sendMessage(Component.text("Spierdalaj"));
      return;
    }

    var foundPlayer = playerManager.firstOnlinePlayer(invocation.arguments()[0]);
    if (foundPlayer == null) {
      invocation.source().sendMessage(Component.text("Player not found!"));
      return;
    }

    var message = Arrays.stream(invocation.arguments()).skip(1).collect(Collectors.joining(" "));
    try {
      foundPlayer.playerExecutor().sendChatMessage(
              Component.text(player.getUsername() + " -> ty: " + message)
      );
    } catch (Exception exception) {
      invocation.source().sendMessage(Component.text("Failed bruhhh"));
      exception.printStackTrace();
      return;
    }
    dataSource.open().thenAccept(conn -> {
      conn.set("reply:" + foundPlayer.uniqueId(),
          String.valueOf(player.getUniqueId()),
          SetParams.setParams().ex(15));
      conn.set("reply:" + player.getUniqueId(),
          String.valueOf(foundPlayer.uniqueId()),
          SetParams.setParams().ex(15));
    });

    invocation.source()
        .sendMessage(Component.text("ty -> " + foundPlayer.name() + ": " + message));
  }
}
