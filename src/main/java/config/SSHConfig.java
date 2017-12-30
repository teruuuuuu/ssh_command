package config;

public class SSHConfig {
  private String host;
  private String hostName;
  private int port;
  private String itentityFile;
  private String user;

  public SSHConfig(String host){
    this.host = host;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getItentityFile() {
    return itentityFile;
  }

  public void setItentityFile(String itentityFile) {
    this.itentityFile = itentityFile;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }
}
