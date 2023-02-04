package pl.memexurer.gaming.identification;

import eu.cloudnetservice.driver.network.buffer.DataBuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class PartyIdentification implements UserIdentification {

    private UUID owner;
    private Collection<UUID> members;

    PartyIdentification() {
    }

    public PartyIdentification(UUID owner, Collection<UUID> members) {
        this.owner = owner;
        this.members = members;
    }

    @Override
    public Collection<UUID> getMembers() {
        return members;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public void writeData(DataBuf.Mutable dataBuf) {
        dataBuf.writeBoolean(true);
        dataBuf.writeUniqueId(owner);
        dataBuf.writeInt(members.size());
        for (UUID member : members) {
            dataBuf.writeUniqueId(member);
        }
    }

    @Override
    public void readData(DataBuf dataBuf) {
        this.owner = dataBuf.readUniqueId();
        int size = dataBuf.readInt();

        List<UUID> uuids = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            uuids.add(dataBuf.readUniqueId());
        }
    }
}