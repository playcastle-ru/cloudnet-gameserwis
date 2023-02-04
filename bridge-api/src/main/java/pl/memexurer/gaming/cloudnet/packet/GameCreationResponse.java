package pl.memexurer.gaming.cloudnet.packet;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import pl.memexurer.gaming.game.GameData;

public class GameCreationResponse extends GameResponse<GameData> {

    public GameCreationResponse(String error) {
        super(GameData.class, error);
    }

    public GameCreationResponse(GameData value) {
        super(GameData.class, value);
    }

    public GameCreationResponse() {
        super(GameData.class);
    }

    @Override
    public void writeData(DataBuf.Mutable dataBuf) {
        dataBuf.writeBoolean(value != null);
        if (value != null) {
            dataBuf.writeString(value.getClass().getName());
            dataBuf.writeObject(value);
        } else {
            dataBuf.writeString(error);
        }
    }

    @Override
    public void readData(DataBuf dataBuf) {
        if (dataBuf.readBoolean()) {
            try {
                this.value = dataBuf.readObject(
                        (Class<? extends GameData>) Class.forName(dataBuf.readString()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.error = dataBuf.readString();
        }
    }
}
