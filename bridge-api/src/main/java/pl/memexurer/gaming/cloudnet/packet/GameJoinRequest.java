package pl.memexurer.gaming.cloudnet.packet;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import pl.memexurer.gaming.identification.UserIdentification;

public class GameJoinRequest extends GameRequest {

    private UserIdentification identification;

    public GameJoinRequest() {
    }

    public GameJoinRequest(String gameId,
                           UserIdentification identification) {
        super(gameId);
        this.identification = identification;
    }

    public UserIdentification getIdentification() {
        return identification;
    }

    @Override
    public void writeData(DataBuf.Mutable dataBuf) {
        super.writeData(dataBuf);
        dataBuf.writeObject(identification);
    }

    @Override
    public void readData(DataBuf dataBuf) {
        super.readData(dataBuf);
        this.identification = UserIdentification.decode(dataBuf);
    }
}
