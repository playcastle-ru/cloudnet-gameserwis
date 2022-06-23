package pl.memexurer.gaming.chat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;

public class MsgCommand implements SimpleCommand {

  @Override
  public void execute(Invocation invocation) {
    if (!(invocation.source() instanceof Player player)) {
      invocation.source().sendMessage(Component.text("Spierdalaj"));
      return;
    }

    var foundPlayer = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(
            IPlayerManager.class)
        .getFirstOnlinePlayer(invocation.arguments()[0]);
    if (foundPlayer == null) {
      invocation.source().sendMessage(Component.text("Player not found!"));
      return;
    }

    var message = Arrays.stream(invocation.arguments()).skip(1).collect(Collectors.joining(" "));
    try {
      foundPlayer.getPlayerExecutor().sendChatMessage(player.getUsername() + " -> ty: " + message);
    } catch (Exception exception) {
      invocation.source().sendMessage(Component.text("Failed bruhhh"));
      exception.printStackTrace();
      return;
    }

    conn.set("reply:" + foundPlayer.getUniqueId(),
        String.valueOf(player.getUniqueId()),
        SetParams.setParams().ex(15));
    conn.set("reply:" + player.getUniqueId(),
        String.valueOf(foundPlayer.getUniqueId()),
        SetParams.setParams().ex(15));

    invocation.source()
        .sendMessage(Component.text("ty -> " + foundPlayer.getName() + ": " + message));
  }
}
