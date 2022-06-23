package pl.memexurer.gaming;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.dytanic.cloudnet.ext.bridge.velocity.event.VelocityChannelMessageReceiveEvent;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import pl.memexurer.gaming.cloudnet.ManagedServer;
import pl.memexurer.gaming.cloudnet.joiner.PlayerGameJoinPacket;
import pl.memexurer.gaming.identification.PartyIdentification;
import pl.memexurer.gaming.identification.PlayerIdentification;
import pl.memexurer.gaming.identification.UserIdentification;
import pl.multiportalmc.party.api.PartyHandler.Party;
import pl.multiportalmc.party.api.PartyMember;
import pl.multiportalmc.party.velocity.PartyPlugin;

@Plugin(id = "gaming")
public class GamingVelocityPlugin {

  @Inject
  private ProxyServer server;

  @Subscribe // player forwarding for servers managed by this plugin
  public void onMessage(VelocityChannelMessageReceiveEvent event) {
    if (event.getChannel().equals(PlayerGameJoinPacket.CHANNEL)) {
      var packet = event.getBuffer().readObject(PlayerGameJoinPacket.class);
      var player = server.getPlayer(packet.getUuid()).orElseThrow();
      player.sendMessage(
          Component.text("Dobra czekaj, probujemy cie przerzucic do giery...", Style.style(
              NamedTextColor.GRAY)));

      var id = PartyPlugin.getHandler().findPlayerParty(packet.getUuid())
          .map((Function<Party, UserIdentification>) party1 -> new PartyIdentification(
              party1.getOwner(),
              party1.getMembers().stream().map(PartyMember::uuid).toList()))
          .orElse(new PlayerIdentification(packet.getUuid()));
      ManagedServer.createConnectionRequest(packet.getServerId(), packet.getGameId(), id)
          .whenComplete(
              (unused, throwable) -> {
                if (throwable != null) {
                  player.sendMessage(Component.text("Blad: " + throwable.getMessage(), Style.style(
                      NamedTextColor.RED)));
                }
              });
    }
  }

  /*
  player forwarding for unmanaged servers without any party plugins (disabled for now)
  @Subscribe
  private void checkMoveParty(ServerPreConnectEvent event) {
    var targetServerName = event.getResult().getServer()
        .map(RegisteredServer::getServerInfo)
        .map(ServerInfo::getName)
        .filter(name -> name.startsWith("Dupsco"))
        .orElse(null);
    if (targetServerName == null) {
      return;
    }

    var party = PartyPlugin.getHandler().findPlayerParty(event.getPlayer().getUniqueId())
        .orElse(null);
    if (party == null) {
      return;
    }

    var partyOwner = CloudNetDriver.getInstance().getServicesRegistry()
        .getFirstService(IPlayerManager.class)
        .getOnlinePlayer(party.getOwner());
    if (partyOwner == null) {
      event.getPlayer().sendMessage(Component.text("Nie mozesz samemu zmieniac serwerow w party."));
      event.setResult(ServerPreConnectEvent.ServerResult.denied());
      return;
    }

    event.getPlayer().sendMessage(Component.text(partyOwner.getConnectedService().getServerName()));
    event.getPlayer().sendMessage(Component.text(targetServerName));

    if (!partyOwner.getConnectedService().getServerName().equals(targetServerName)
        && !party.getOwner().equals(event.getPlayer().getUniqueId())) {
      event.getPlayer().sendMessage(Component.text("Nie mozesz samemu zmieniac serwerow w party."));
      event.setResult(ServerPreConnectEvent.ServerResult.denied());
    }
  }

   @Subscribe
  private void moveParty(ServerConnectedEvent event) {
    var targetServerName = event.getServer().getServerInfo().getName();
    if (!targetServerName.startsWith("Bedwars-")) {
      return;
    }

    var party = PartyPlugin.getHandler()
        .findPlayerParty(event.getPlayer().getUniqueId());
    if (party.isPresent() && party.get().getOwner().equals(event.getPlayer().getUniqueId())) {
      server.getScheduler().buildTask(this, () -> {
        for (var member : party.get().getMembers()) {
          if (member.uuid().equals(party.get().getOwner())) {
            continue;
          }

          server.getPlayer(member.uuid()).ifPresentOrElse(
              value -> {
                value.createConnectionRequest(event.getServer())
                    .fireAndForget();
                server.sendMessage(Component.text("Elo elo, wyslamy " + member.uuid() + " przez velocity na " + event.getServer()));
              },
              () -> {
                UserIdentification.joinServer(member.uuid(), targetServerName);
                server.sendMessage(Component.text("Elo elo, wyslamy " + member.uuid() + " przez cloudneta na " + targetServerName));
              });
        }
      }).delay(1000, TimeUnit.MILLISECONDS).schedule();
    }
  }
   */


}
