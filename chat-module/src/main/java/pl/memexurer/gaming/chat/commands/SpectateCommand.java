package pl.memexurer.gaming.chat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import eu.cloudnetservice.driver.channel.ChannelMessage;
import eu.cloudnetservice.driver.channel.ChannelMessageTarget;
import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import net.kyori.adventure.text.Component;

public class SpectateCommand implements SimpleCommand {
  private final PlayerManager playerManager;
  private final CloudServiceProvider serviceProvider;

  public SpectateCommand(PlayerManager playerManager,
                         CloudServiceProvider serviceProvider) {
    this.playerManager = playerManager;
    this.serviceProvider = serviceProvider;
  }

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

    var foundPlayer = playerManager.firstOnlinePlayer(invocation.arguments()[0]);

    if (foundPlayer == null) {
      invocation.source().sendMessage(
          Component.text("Produkt zostal zabrany ze strefy pakowania. Poczekaj na pomoc."));
      return;
    }

    if (foundPlayer.connectedService().serverName().startsWith("Bedwars-")) {
      var collection = serviceProvider
          .servicesByGroup("BedwarsLobby");

      var buffer = DataBuf.empty();
      buffer.writeUniqueId(player.getUniqueId());
      buffer.writeString(foundPlayer.name());

      ChannelMessage.builder()
          .channel("spectate_a_nigger")
          .target(ChannelMessageTarget.Type.SERVICE, collection.iterator().next().name())
          .buffer(buffer)
          .build().send();
      invocation.source().sendMessage(Component.text("Wyslano zapytanie o spectatowanie, poczekaj..."));
    } else {
      invocation.source().sendMessage(
          Component.text("Produkt nie zostal znaleziony w bazie artykulow. Poczekaj na pomoc."));
    }
  }
}
