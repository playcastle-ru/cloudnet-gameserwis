package pl.memexurer.gaming.cloudnet.joiner;

import eu.cloudnetservice.driver.channel.ChannelMessage;
import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufable;
import eu.cloudnetservice.modules.bridge.node.player.NodePlayerManager;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import pl.memexurer.gaming.game.Game;

import java.util.UUID;

public class PlayerGameJoinPacket implements DataBufable {
    public static final String CHANNEL = "7yOPE5LvpkynNqCk7nLEwl";

    private UUID uuid;
    private String serverId;
    private String gameId;

    private PlayerGameJoinPacket(UUID uuid, String serverId, String gameId) {
        this.uuid = uuid;
        this.serverId = serverId;
        this.gameId = gameId;
    }

    public PlayerGameJoinPacket(UUID player, Game game) {
        this.uuid = player;
        this.serverId = game.parent();
        this.gameId = game.id();
    }

    public PlayerGameJoinPacket() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getServerId() {
        return serverId;
    }

    public String getGameId() {
        return gameId;
    }

    @Override
    public void writeData(DataBuf.Mutable dataBuf) {
        dataBuf.writeUniqueId(uuid);
        dataBuf.writeString(serverId);
        dataBuf.writeString(gameId);
    }

    @Override
    public void readData(DataBuf dataBuf) {
        this.uuid = dataBuf.readUniqueId();
        this.serverId = dataBuf.readString();
        this.gameId = dataBuf.readString();
    }
}
