package Connector;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Vector;

public class UDPMulticast {

	public final static int MAXSIZE=32;
	private boolean stop = false;
	private Vector delegates = new Vector();
	private MulticastSocket ms;
	private InetAddress sessAddr ;
	private int port;
	
	public void delDelegate(UDPMulticastDelegate delegate)
	{
		this.delegates.remove(delegate);
		if (this.delegates.size() == 0)
		{	
			stop = true;
			try {
				this.sendData(new UDPString());
			} catch (IOException e) {
				// Impossibilit� de bien tout faire !
				e.printStackTrace();
			}
		}
	}
	
	public void setDelegate(UDPMulticastDelegate delegate)
	{
		// Ajout d'un nouveau delegu� pour la r�ception de message.
		this.delegates.add(delegate);
	}
	
	public void sendData(UDPString buffer) throws IOException
	{
		DatagramPacket p = new DatagramPacket(buffer.getBytes(), buffer.getBytes().length, sessAddr, this.port);
		
		try {
			//System.out.println("Sending data : " + buffer.toString());
			this.ms.send(p);
		} catch (IOException e) {
			// Erreur lors de l'envoi du packet multicast...
			throw new IOException("sendData:" + e);
		}
	}
	
	
	
	public UDPMulticast (String IP, int Port) throws IOException{
		
		try {
			this.port = Port;
			this.ms =  new MulticastSocket(Port);
			sessAddr = InetAddress.getByName(IP);
			ms.joinGroup(sessAddr);
			Thread listener = new Thread(new ThreadUDPListener());
			listener.start();
			// puis on lance un thread d'�coute sur cette session, et l'on reforward le message aux delegates...
		} catch (IOException e) {
			throw new IOException("UDPMulticast():" + e);
			
		}
	}
	
	public class ThreadUDPListener implements Runnable{

		public void run() 
		{
			
			while (!stop)
			{
				
				try {
					byte[] buf = new byte[MAXSIZE];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					ms.receive(packet);
					//Iterator it = delegates.iterator();
					String message = new String ();
					for (int i=0; i < packet.getLength(); i+=1)
					{	
						char c = (char) packet.getData()[i];
						message += c;
					}
					
					for (int i=0; i < delegates.size() ; i+=1)
					{
						((UDPMulticastDelegate)delegates.get(i)).receivedData(new UDPString(message), packet);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
	
}
