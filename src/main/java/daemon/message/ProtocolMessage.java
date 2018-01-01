package daemon.message;

import java.io.Serializable;

public class ProtocolMessage implements Serializable {

  public int messageType;
  public String host;
  public String command;

  public ProtocolMessage(int messageType, String host, String command) {
    this.messageType = messageType;
    this.host = host;
    this.command = command;
  }
}
