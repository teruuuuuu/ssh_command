package command;

import config.LoadSshConfig;
import config.SSHConfig;
import net.schmizz.sshj.SSHClient;
import util.CommandOverSsh;
import util.SshConnect;

import java.io.IOException;
import java.util.Map;

public class SshCommand {
  String host;
  String command;

  public SshCommand(String... args){
    String host = args[0];
    String command = "";
    for(int i=1; i < args.length; i++){
      command = command + " " + args[i];
    }
    this.host = host;
    this.command = command;
  }

  public static void main(String... args) throws IOException{
    SshCommand command = new SshCommand(args);
    command.run();
    return;
  }

  public void run(){
    try {
      Map<String, SSHConfig> sshConfigMap = new LoadSshConfig().loadConfig();
      SSHConfig sshConfig = sshConfigMap.get(host);
      SSHClient sshClient = SshConnect.sshKeyConnectSetting(host, sshConfig.getItentityFile(), sshConfig.getUser());
      System.out.println(CommandOverSsh.sessionCommand(sshClient.startSession(), command, 5));

      try{
        sshClient.disconnect();
      }finally {
        sshClient.disconnect();
      }
    }catch(IOException e){
      e.printStackTrace();
    }
    return;
  }
}
