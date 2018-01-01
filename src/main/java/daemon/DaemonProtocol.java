package daemon;

import daemon.message.ProtocolMessage;
import util.ArrayBuffer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

import static daemon.message.MessageTypeDefine.MESSAGE_UNDEFINED;

public class DaemonProtocol implements Callable<ProtocolMessage> {
  static public final int BUFSIZE = 32;
  private Socket clntSock;

  public DaemonProtocol(Socket clntSock){
    this.clntSock = clntSock;
  }
  @Override
  public ProtocolMessage call() {
    System.out.println("Client address and port = + " + clntSock.getInetAddress().getHostAddress() + ":" + clntSock.getPort());
    System.out.println("Thread = " + Thread.currentThread().getName());

    try{
      // 受け取ったコマンドの識別
      System.out.println("serv receive start");
      ProtocolMessage message = byteToMessage(clntSock);
      System.out.println("serv receive end");
      // 判別したコマンドを返す
      return message;
    } catch (DaemonException e){
      e.printStackTrace();
    }
    return new ProtocolMessage(MESSAGE_UNDEFINED, "","");
  }

  private ProtocolMessage byteToMessage(Socket clntSock) throws DaemonException{
    try {
      return (ProtocolMessage)ArrayBuffer.toObject(StreamReader.streamToByte(clntSock.getInputStream(), BUFSIZE));
    }catch (IOException e){
      e.printStackTrace();
    }catch (ClassNotFoundException e){
      e.printStackTrace();
    }
    throw new DaemonException("クライアントからのリクエストを判別できませんでした");
  }

}
