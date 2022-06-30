package pl.memexurer.gaming.identification;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.serialization.SerializableObject;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import java.util.Collection;
import java.util.UUID;

public interface UserIdentification extends SerializableObject {

  Collection<UUID> getMembers();

  default void joinServer(String id) {
    for(var member: getMembers()) {
      joinServer(member, id);
    }
  }

  static void joinServer(UUID uuid, String id) {
    var player = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class)
        .getOnlinePlayer(uuid);
    if(player == null)
      throw new IllegalArgumentException("Fuck you! Specified player is not here.");

    if(player.getConnectedService().getServerName().equals(id))
      return;

    player.getPlayerExecutor().connect(id);
  }

  static UserIdentification decode(ProtocolBuffer buffer) {
    if(buffer.readBoolean()) {
      var party = new PartyIdentification();
      party.read(buffer);
      return party;
    } else {
      var player = new PlayerIdentification();
      player.read(buffer);
      return player;
    }
  }
}
