package command;

import daemon.DaemonClient;
import daemon.message.ProtocolMessage;
import util.ArrayBuffer;

import java.io.IOException;
import java.net.ConnectException;

import static daemon.message.MessageTypeDefine.*;

public class SshProcess {
  int port = 50190;
  String host;
  String command;

  public SshProcess(String... args){
    String host = args[0];
    String command = "";
    for(int i=1; i < args.length; i++){
      command = command + " " + args[i];
    }
    this.host = host;
    this.command = command;
  }

  public static void main(String... args) {
    SshProcess command = new SshProcess(args);
    command.run();
    return;
  }

  public void run(){
    try {
      // デーモンの生存確認
      DaemonClient.daemonCommand("localhost",
              ArrayBuffer.fromObject(new ProtocolMessage(DAEMON_LIVING, "", "")), this.port);
    }catch (ConnectException e){
      // デーモンなし
      System.out.println("常駐プロセスを先に起動してください。");
    }catch (IOException e){
      e.printStackTrace();
    }
    try{
      String response = DaemonClient.daemonCommand("localhost",
              ArrayBuffer.fromObject(new ProtocolMessage(COMMAND, this.host, this.command)), port);
      System.out.println(response);
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}
