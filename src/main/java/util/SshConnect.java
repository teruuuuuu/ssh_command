package util;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import java.io.File;
import java.io.IOException;

public class SshConnect {
  public static SSHClient sshKeyConnectSetting(String host, String sshKeyFilePath, String username) throws IOException {
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
}
