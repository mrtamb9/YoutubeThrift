package thrift.test;

import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import thrift.server.InterfaceYoutubeThrift;

public class ThriftClient {
	public static void main(String [] args)
	{
		while(true)
		{
			System.out.println("Client 3 request ...");
			try {
				TTransport transport;
				transport = new TSocket("localhost", 1111);
				transport.open();
				TProtocol protocol = new TBinaryProtocol(transport);
				InterfaceYoutubeThrift.Client client = new InterfaceYoutubeThrift.Client(protocol);
				List<String> listIPs = client.gettingAllIP("2.2.2.2 thanhtam1234");
				for(String row : listIPs) {
					System.out.println(row);
				}
				transport.close();

			} catch (TException x) {
				x.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
