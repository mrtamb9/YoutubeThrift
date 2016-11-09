package service;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class ServerSearchEventByHashtags {

  public static HandlerSearchEventByHashtags handler;
  public static ServiceSearchEventByHashtags.Processor<HandlerSearchEventByHashtags> processor;
  
  public static void main(String [] args) {
    try {
      handler = new HandlerSearchEventByHashtags();
      processor = new ServiceSearchEventByHashtags.Processor<HandlerSearchEventByHashtags>(handler);

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

  public static void simple(ServiceSearchEventByHashtags.Processor<HandlerSearchEventByHashtags> processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(4000);
      TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

      System.out.println("Search event by hashtags server. Port 4000");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
