package pl.memexurer.gaming.identification;

import eu.cloudnetservice.driver.network.buffer.DataBuf;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public final class PlayerIdentification implements UserIdentification {

    private UUID user;

    public PlayerIdentification(UUID user) {
        this.user = user;
    }

    PlayerIdentification() {
    }

    @Override
    public Collection<UUID> getMembers() {
        return Collections.singletonList(user);
    }

    public UUID getUser() {
        return user;
    }

    @Override
    public void writeData(DataBuf.Mutable dataBuf) {
        dataBuf.writeBoolean(false);
        dataBuf.writeUniqueId(user);
    }

    @Override
    public void readData(DataBuf dataBuf) {
        this.user = dataBuf.readUniqueId();
    }
}