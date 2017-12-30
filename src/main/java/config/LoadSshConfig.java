package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LoadSshConfig {
  String SPACE = " ";
  public Map<String, SSHConfig> loadConfig() throws IOException{
    Map<String, SSHConfig> ret = new HashMap<String, SSHConfig>();
    BufferedReader br = Files.newBufferedReader(Paths.get(sshConfigFilePath()), StandardCharsets.UTF_8);
    String str;

    String currentHost = "";
    while((str = readLine(br.readLine())) != null){
      String[] configData = str.split(SPACE);
      if(configData[0].trim().equals("Host")){
        currentHost = configData[1];
        ret.put(configData[1], new SSHConfig(configData[1].trim()));
      }else{
        if(configData[0].trim().equals("HostName")){
          ret.get(currentHost).setHostName(configData[1].trim());
        }else if(configData[0].trim().equals("Port")){
          ret.get(currentHost).setPort(Integer.valueOf(configData[1].trim()));
        }else if(configData[0].trim().equals("IdentityFile")){
          if(configData[1].trim().substring(0, 2).equals("~/")){
            ret.get(currentHost).setItentityFile(System.getProperty("user.home") + File.separator + configData[1].trim().substring(2));
          }else{
            ret.get(currentHost).setItentityFile(configData[1].trim());
          }
        }else if(configData[0].trim().equals("User")){
          ret.get(currentHost).setUser(configData[1].trim());
        }
      }
    }
    return ret;
  }

  private String sshConfigFilePath(){
    return System.getProperty("user.home") + File.separator + ".ssh" + File.separator + "config";
  }

  private String readLine(String line){
    if(line == null) return null;
    String ret = "";
    String trimLine = line.contains(SPACE) ? line.trim() : line;
    boolean spaceRead = false;
    for(int i = 0; i < trimLine.length(); i++){
      if(trimLine.charAt(i) == SPACE.charAt(0) && spaceRead ){
        continue;
      }

      ret += trimLine.charAt(i);

      if(trimLine.charAt(i) == SPACE.charAt(0)){
        spaceRead = true;
      } else {
        spaceRead = false;
      }
    }
    return ret;
  }
}
