package daemon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class DaemonClient {
  static final int BUFSIZE = 32;

  public static void main(String... args) throws IOException {
    if ((args.length < 2) || (args.length > 3))
      throw new IllegalArgumentException("Parameter(s):<Server> <Word> [<Port>]");

    DaemonClient.daemonCommand(args[0], args[1].getBytes(), Integer.parseInt(args[2]));
  }

  public static String daemonCommand(String server, byte[] byteBuffer, int port) throws IOException{
    // サーバソケットの初期化
    Socket socket = new Socket(server, port);

    // メッセージ送信
    OutputStream out = socket.getOutputStream();
    out.write(Endcode.withEndcode(byteBuffer));

    // レスポンス受信
    return new String(StreamReader.streamToByte(socket.getInputStream(), BUFSIZE), "UTF-8");
  }

}
