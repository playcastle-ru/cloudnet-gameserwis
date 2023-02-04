package pl.memexurer.gaming.chat.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import net.kyori.adventure.text.Component;
import pl.memexurer.jedisdatasource.api.JedisDataSource;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

public class ReplyCommand implements SimpleCommand {

    private final JedisDataSource dataSource;
    private final PlayerManager playerManager;

    public ReplyCommand(JedisDataSource dataSource,
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

        dataSource.open().thenAccept(conn -> {
            String uuid = conn.get("reply:" + player.getUniqueId());
            if (uuid == null) {
                invocation.source()
                        .sendMessage(Component.text("Nie pisales z nikim przez ostatnie 15 mint? czy cos xd"));
                return;
            }

            var foundPlayer = playerManager.onlinePlayer(UUID.fromString(uuid));
            if (foundPlayer == null) {
                invocation.source()
                        .sendMessage(Component.text("Gracz z ktorym wczesniej pisales jest offline."));
                conn.del("reply:" + player.getUniqueId());
                return;
            }

            var message = String.join(" ", invocation.arguments());
            try {
                foundPlayer.playerExecutor()
                        .sendChatMessage(Component.text(player.getUsername() + " -> ty: " + message));
            } catch (Exception exception) {
                invocation.source().sendMessage(Component.text("Failed bruhhh"));
                exception.printStackTrace();
                return;
            }

            conn.set("reply:" + foundPlayer.uniqueId(),
                    String.valueOf(player.getUniqueId()),
                    SetParams.setParams().ex(15));
            conn.set("reply:" + player.getUniqueId(),
                    String.valueOf(foundPlayer.uniqueId()),
                    SetParams.setParams().ex(15));

            invocation.source()
                    .sendMessage(Component.text("ty -> " + foundPlayer.name() + ": " + message));
        });

    }
}
