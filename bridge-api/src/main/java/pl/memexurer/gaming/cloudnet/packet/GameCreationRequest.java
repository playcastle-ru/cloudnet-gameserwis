package pl.memexurer.gaming.cloudnet.packet;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufable;
import pl.memexurer.gaming.game.GameModifiers;
import pl.memexurer.gaming.game.generic.GenericGameModifiers;

public class GameCreationRequest extends GameRequest{

  private GameModifiers modifiers;

  public GameCreationRequest() {
  }

  public GameCreationRequest(String id, GameModifiers modifiers) {
    super(id);
    this.modifiers = modifiers;
  }

  public GameModifiers getModifiers() {
    return modifiers;
  }

  @Override
  public void writeData(DataBuf.Mutable dataBuf) {
    dataBuf.writeObject(modifiers);
  }

  @Override
  public void readData(DataBuf dataBuf) {
    this.modifiers = dataBuf.readObject(GenericGameModifiers.class);
  }
}
