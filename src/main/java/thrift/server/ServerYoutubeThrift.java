package thrift.server;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class ServerYoutubeThrift {

  public static HandlerYoutubeThrift handler;
  public static InterfaceYoutubeThrift.Processor<HandlerYoutubeThrift> processor;
  
  public static void main(String [] args) {
    try {
      handler = new HandlerYoutubeThrift();
      processor = new InterfaceYoutubeThrift.Processor<HandlerYoutubeThrift>(handler);

      Runnable simple = new Runnable() {
        public void run() {
          simple(processor);
        }
      };
      new Thread(simple).start();
      
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  public static void simple(InterfaceYoutubeThrift.Processor<HandlerYoutubeThrift> processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(1111);
      TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport)
    		  .maxWorkerThreads(1000) // max connection to thrift at the same time
    		  .processor(processor));

      System.out.println("Youtube thrift server: port 1111");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
