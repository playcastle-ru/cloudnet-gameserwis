package pl.memexurer.gaming.cloudnet.packet;

import de.dytanic.cloudnet.driver.serialization.ProtocolBuffer;
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
  public void write(ProtocolBuffer buffer) {
    super.write(buffer);
    buffer.writeObject(identification);
  }

  @Override
  public void read(ProtocolBuffer buffer) {
    super.read(buffer);
    this.identification = UserIdentification.decode(buffer);
  }
}
