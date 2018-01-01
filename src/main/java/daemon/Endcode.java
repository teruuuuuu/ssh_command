package daemon;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Endcode {
  public static final byte endCode[] = { (byte)0, (byte)0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
          (byte)0, (byte)0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
          (byte)0, (byte)0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
          (byte)0, (byte)0 };

  public static byte[] withEndcode(byte[] message){
    ByteBuffer buffer = ByteBuffer.allocate(message.length + 32);
    return buffer.put(message).put(endCode).array();
  }

  public static boolean isEnd(byte[] buffer, int endCodeNum){
    for(int i = buffer.length -1; i > 0; i -- ) {
      if(buffer[i] == 0){
        endCodeNum += 1;
      } else{
        endCodeNum = 0;
      }

      if (endCodeNum >= 32){
        return true;
      }
    }
    return false;
  }

  public static byte[] eliminate(byte[] buffer) {
    for(int i = buffer.length -1; i > 0; i -- ) {
      if(buffer[i] != 0){
        return Arrays.copyOfRange(buffer, 0, i + 1);
      }
    }
    return new byte[0];
  }

  public static int encCodeSeq(byte[] buffer){
    int endCodeNum = 0;
    for(int i = buffer.length -1; i > 0; i -- ) {
      if(buffer[i] == 0){
        endCodeNum += 1;
      } else {
        return endCodeNum;
      }
    }
    return endCodeNum;
  }
}
