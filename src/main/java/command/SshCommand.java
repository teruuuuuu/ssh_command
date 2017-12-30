package command;

import config.LoadSshConfig;
import config.SSHConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SshCommand {

  public static void main(String... args) throws IOException{
    Map<String, SSHConfig> sshConfigMap = new LoadSshConfig().loadConfig();
    String host = args[0];
    String command = "";
    for(int i=1; i < args.length; i++){
      command = command + " " + args[i];
    }


    SSHConfig sshConfig = sshConfigMap.get(host);
    SSHClient sshClient = sshKeyConnectSetting(host, sshConfig.getItentityFile(), sshConfig.getUser());
    try {
      sessionCommand(sshClient.startSession(), command, 5);
    }finally {
      sshClient.disconnect();
    }
    return;
  }

  private static SSHClient sshKeyConnectSetting(String host, String sshKeyFilePath, String username) throws IOException {
    SSHClient ssh = new SSHClient();
    ssh.loadKnownHosts();
    ssh.connect(host);
    // keepAliveの感覚設定
    ssh.getConnection().getKeepAlive().setKeepAliveInterval(5);
    // 接続時のsshキーの指定はauthPublickeyを認証時のパスワードにはauthPasswordを使う
    KeyProvider keys = ssh.loadKeys(new File(sshKeyFilePath).getPath(), "");
    ssh.authPublickey(username,keys);
    return ssh;
  }

  private static void sessionCommand(Session session, String command, int timeout) throws IOException {
    try {
      final Session.Command cmd = session.exec(command);
      System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
      cmd.join(timeout, TimeUnit.SECONDS);
      System.out.println("\n** exit status: " + cmd.getExitStatus());
    } finally {
      session.close();
    }
  }
}
