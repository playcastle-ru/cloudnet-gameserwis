package pl.memexurer.gaming.cloudnet.packet;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufable;

public abstract class GameResponse<T extends DataBufable> implements DataBufable {

    private final Class<T> baseClass;
    protected T value;
    protected String error;

    protected GameResponse(Class<T> baseClass) {
        this.baseClass = baseClass;
    }

    protected GameResponse(Class<T> baseClass, String error) {
        if (error == null)
            throw new IllegalArgumentException("error cannot be null, fuck off");
        this.baseClass = baseClass;
        this.error = error;
    }

    protected GameResponse(Class<T> baseClass, T value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null, fuck off");
        this.baseClass = baseClass;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return error;
    }

    @Override
    public void writeData(DataBuf.Mutable dataBuf) {
        if (value == null) {
            dataBuf.writeBoolean(false);
        } else {
            dataBuf.writeBoolean(true);
            dataBuf.writeObject(value);
        }

        if (error == null) {
            dataBuf.writeBoolean(false);
        } else {
            dataBuf.writeBoolean(true);
            dataBuf.writeString(error);
        }
    }

    @Override
    public void readData(DataBuf dataBuf) {
        if (dataBuf.readBoolean()) {
            this.value = dataBuf.readObject(baseClass);
        }
        if (dataBuf.readBoolean()) {
            this.error = dataBuf.readString();
        }
    }
}
