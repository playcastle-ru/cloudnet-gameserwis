package pl.memexurer.gaming.identification;

import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufable;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;

import java.util.Collection;
import java.util.UUID;

public interface UserIdentification extends DataBufable {

    static void joinServer(UUID uuid, String id) {
        var player = InjectionLayer.ext().instance(ServiceRegistry.class)
                .firstProvider(PlayerManager.class)
                .onlinePlayer(uuid);
        if (player == null)
            throw new IllegalArgumentException("Fuck you! Specified player is not here.");

        if (player.connectedService().serverName().equals(id))
            return;

        player.playerExecutor().connect(id);
    }

    static UserIdentification decode(DataBuf buffer) {
        if (buffer.readBoolean()) {
            var party = new PartyIdentification();
            party.readData(buffer);
            return party;
        } else {
            var player = new PlayerIdentification();
            player.readData(buffer);
            return player;
        }
    }

    Collection<UUID> getMembers();

    default void joinServer(String id) {
        for (var member : getMembers()) {
            joinServer(member, id);
        }
    }
}
