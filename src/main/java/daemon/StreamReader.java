package daemon;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class StreamReader {

  public static byte[] streamToByte(InputStream in, int bufsize) throws IOException{
    int MESSAGE_SIZE = 1024 * 1024;
    int recvMsgSize;
    ByteBuffer byteBuffSum = ByteBuffer.allocate(MESSAGE_SIZE);
    byte[] buffer = new byte[bufsize];
    int endCodeNum = 0;
    // クライアントが接続をクローズするまで受信する
    while ((recvMsgSize = in.read(buffer)) != -1) {
      byteBuffSum.put(buffer);
      if(Endcode.isEnd(buffer, endCodeNum))
        break;
      endCodeNum = Endcode.encCodeSeq(buffer);
      buffer = new byte[bufsize];
    }

    return Endcode.eliminate(byteBuffSum.array());
  }
}
