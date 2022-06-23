package pl.memexurer.gaming.chat;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.dytanic.cloudnet.ext.bridge.velocity.event.VelocityChannelMessageReceiveEvent;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import pl.memexurer.gaming.chat.commands.MsgCommand;
import pl.memexurer.gaming.chat.commands.ReplyCommand;
import pl.memexurer.gaming.chat.commands.SimpleCommandMeta;
import pl.memexurer.gaming.cloudnet.ManagedServer;
import pl.memexurer.gaming.cloudnet.joiner.PlayerGameJoinPacket;
import pl.memexurer.gaming.identification.PartyIdentification;
import pl.memexurer.gaming.identification.PlayerIdentification;
import pl.memexurer.gaming.identification.UserIdentification;
import pl.multiportalmc.party.api.PartyHandler.Party;
import pl.multiportalmc.party.api.PartyMember;
import pl.multiportalmc.party.velocity.PartyPlugin;

@Plugin(id = "gaming-chat")
public class GamingVelocityPlugin {

  @Inject
  private ProxyServer server;

  @Subscribe
  public void onInit(ProxyInitializeEvent event) {
    server.getCommandManager().register(new SimpleCommandMeta(this, "msg", "pm"), new MsgCommand());
    server.getCommandManager().register(new SimpleCommandMeta(this, "reply", "r"), new ReplyCommand());
  }
\

}
