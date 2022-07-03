package pl.memexurer.gaming.chat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import de.dytanic.cloudnet.driver.channel.ChannelMessageTarget.Type;
import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;

public class SpectateCommand implements SimpleCommand {

  @Override
  public void execute(Invocation invocation) {
    if (!(invocation.source() instanceof Player player) || !player.hasPermission("core.spectate")) {
      invocation.source().sendMessage(Component.text("Spierdalaj"));
      return;
    }

    if (invocation.arguments().length != 1) {
      invocation.source().sendMessage(Component.text("Umiesc artykul w strefie pakowania. (nick)"));
      return;
    }

    var foundPlayer = CloudNetDriver.getInstance()
        .getServicesRegistry()
        .getFirstService(IPlayerManager.class)
        .getFirstOnlinePlayer(invocation.arguments()[0]);

    if (foundPlayer == null) {
      invocation.source().sendMessage(
          Component.text("Produkt zostal zabrany ze strefy pakowania. Poczekaj na pomoc."));
      return;
    }

    if (foundPlayer.getConnectedService().getServerName().startsWith("Bedwars-")) {
      var collection = CloudNetDriver.getInstance()
          .getCloudServiceProvider()
          .getCloudServicesByGroup("BedwarsLobby");

      var buffer = ProtocolBuffer.create();
      buffer.writeUUID(player.getUniqueId());
      buffer.writeString(foundPlayer.getName());

      ChannelMessage.builder()
          .channel("spectate_a_nigger")
          .target(Type.SERVICE, collection.iterator().next().getName())
          .buffer(buffer)
          .build().send();
      invocation.source().sendMessage(Component.text("Wyslano zapytanie o spectatowanie, poczekaj..."));
    } else {
      invocation.source().sendMessage(
          Component.text("Produkt nie zostal znaleziony w bazie artykulow. Poczekaj na pomoc."));
    }
  }
}
