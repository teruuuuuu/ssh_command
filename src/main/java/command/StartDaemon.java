package command;

import daemon.DaemonServ;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartDaemon {
  public static void main(String... args) throws IOException{

    int port = 50190;
    ExecutorService exec = Executors.newCachedThreadPool();
    exec.submit(new DaemonServ(new ServerSocket(port)));
  }
}
