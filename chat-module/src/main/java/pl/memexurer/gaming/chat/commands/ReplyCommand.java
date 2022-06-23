package pl.memexurer.gaming.chat.commands;

import static pl.memexurer.trevorcore.msg.MsgPayload.CHANNEL_NAME;

import co.schemati.trevor.api.TrevorService;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import pl.memexurer.trevorcore.TrevorCore;
import pl.memexurer.trevorcore.msg.MsgPayload;
import pl.memexurer.trevorcore.msg.MsgState;

public class ReplyCommand implements SimpleCommand {

  @Override
  public void execute(Invocation invocation) {
    if (!(invocation.source() instanceof Player player)) {
      invocation.source().sendMessage(Component.text("Spierdalaj"));
      return;
    }

    String uuid = conn.get("reply:" + player.getUniqueId());
    if (uuid == null) {
      invocation.source()
          .sendMessage(Component.text("Nie pisales z nikim przez ostatnie 15 mint? czy cos xd"));
      return;
    }

    var foundPlayer = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(
        IPlayerManager.class)
        .getOnlinePlayer(UUID.fromString(uuid));
    if (foundPlayer == null) {
      invocation.source()
          .sendMessage(Component.text("Gracz z ktorym wczesniej pisales jest offline."));
      conn.del("reply:" + player.getUniqueId());
      return;
    }

    var message = String.join(" ", invocation.arguments());
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
