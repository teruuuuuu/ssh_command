package util;

import daemon.Endcode;
import daemon.StreamReader;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class CommandOverSsh {
  public static String sessionCommand(Session session, String command, int timeout) throws IOException {
    try {
      final Session.Command cmd = session.exec(command);
      String response = new String(StreamReader.streamToByte(cmd.getInputStream(), 32));
      cmd.join(timeout, TimeUnit.SECONDS);
      return response;
    } finally {
      session.close();
    }
  }
}
