package daemon;

import config.LoadSshConfig;
import config.SSHConfig;
import daemon.message.ProtocolMessage;
import net.schmizz.sshj.SSHClient;
import util.CommandOverSsh;
import util.SshConnect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static daemon.message.MessageTypeDefine.COMMAND;

public class DaemonServ implements Runnable{
  ServerSocket serverSocket;
  ExecutorService exec = Executors.newCachedThreadPool();
  Map<String, SSHConfig> sshConfigMap = new HashMap<String, SSHConfig>();
  Map<String, SSHClient> sshClientMap = new HashMap<String, SSHClient>();
  Map<String, String> currentDirMap = new HashMap<String, String>();

  public DaemonServ(ServerSocket serverSocket) throws IOException {
    sshConfigMap = new LoadSshConfig().loadConfig();
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    for (;;) {
      try{
        Socket clntSock = serverSocket.accept();
        Future<ProtocolMessage> messageResponse = exec.submit(new DaemonProtocol(clntSock));
        // 受信したメッセージを判別
        ProtocolMessage message = messageResponse.get();
        String responseMessage = execCommand(message);
        // クライアントの結果を送信
        clntSock.getOutputStream().write(Endcode.withEndcode(responseMessage.getBytes()));
        // クライアント接続の解放
        clntSock.close();
      } catch (IOException e){
        e.printStackTrace();
      } catch (ExecutionException e){
        e.printStackTrace();
      } catch (InterruptedException e){
        e.printStackTrace();
      }
    }
  }

  public String execCommand(ProtocolMessage message) throws IOException{
    if(message.messageType == COMMAND) {
      if(!sshClientMap.containsKey(message.host)){
        SSHConfig sshConfig = sshConfigMap.get(message.host);
        SSHClient sshClient = SshConnect.sshKeyConnectSetting(message.host, sshConfig.getItentityFile(), sshConfig.getUser());
        sshClientMap.put(message.host,  sshClient);
        currentDirMap.put(message.host,  "");
      }
      String response = CommandOverSsh.sessionCommand(sshClientMap.get(message.host).startSession(),
              "cd " +currentDirMap.get(message.host) + "; " + message.command + " ; pwd", 5);
      String[] result = responseWithPath(response);
      currentDirMap.put(message.host,  result[1]);
      return result[0];
    }

    return "command exec fail";
  }


  public String[] responseWithPath(String response){
    String[] result = response.split("\n");
    String commandResult = "";
    String path = "";
    for(int i = 0; i <= result.length - 2; i++){
      commandResult += result[i] + "\n";
    }

    byte[] a = {(byte)0};
    path = result[result.length - 1].replace(new String(a), "");
    String[] ret = {commandResult, path};
    return ret;
  }
}
