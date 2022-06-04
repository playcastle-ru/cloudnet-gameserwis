package pl.memexurer.gaming.cloudnet.packet;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
import pl.memexurer.gaming.game.GameModifiers;
import pl.memexurer.gaming.game.castlemode.CastlemodeGameModifiers;

public class GameCreationRequest extends GameRequest {

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
  public void write(ProtocolBuffer buffer) {
    super.write(buffer);
    buffer.writeObject(modifiers);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    super.read(buffer);
    this.modifiers = buffer.readObject(CastlemodeGameModifiers.class);
  }
}
