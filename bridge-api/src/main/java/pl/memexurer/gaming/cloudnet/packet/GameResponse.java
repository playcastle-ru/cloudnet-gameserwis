package pl.memexurer.gaming.cloudnet.packet;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import de.dytanic.cloudnet.driver.serialization.SerializableObject;

public abstract class GameResponse<T extends SerializableObject> implements SerializableObject {

  private final Class<T> baseClass;
  protected T value;
  protected String error;

  protected GameResponse(Class<T> baseClass) {
    this.baseClass = baseClass;
  }

  protected GameResponse(Class<T> baseClass, String error) {
    if(error == null)
      throw new IllegalArgumentException("error cannot be null, fuck off");
    this.baseClass = baseClass;
    this.error = error;
  }

  protected GameResponse(Class<T> baseClass, T value) {
    if(value == null)
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
  public void write(ProtocolBuffer buffer) {
    buffer.writeOptionalObject(value);
    buffer.writeOptionalString(error);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    this.value = buffer.readOptionalObject(baseClass);
    this.error = buffer.readOptionalString();
  }
}
