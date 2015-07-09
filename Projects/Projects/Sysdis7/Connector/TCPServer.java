package Connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;

public class TCPServer {

	private int port;
	private ServerSocket s;
	private boolean stop=false;
	private TCPServerDelegate delegate = null;
	
	public void setDelegate(TCPServerDelegate delegate)
	{
		// soit un nouveau delegate => celui-ci est unique
		if (this.delegate == null)
			this.delegate = delegate;
	}
	
	public void delDelegate(TCPServerDelegate delegate)
	{
		if (this.delegate == delegate)
		{
			this.delegate = null;
			this.stop = true;
			// on envoi un message pour arr�ter l'�coute !
			try {
				Socket s = new Socket(InetAddress.getLocalHost(), this.port);
				
			} catch (UnknownHostException e) {
				// ouille, gros probl�me !
				e.printStackTrace();
			} catch (IOException e) {
				// ouille, gros probl�me !
				e.printStackTrace();
			}
		}
	}
	
	public TCPServer(int port) throws IOException
	{
		this.port = port;
		try {
			
			s = new ServerSocket();
			s.setReuseAddress(true);
			s.bind(new java.net.InetSocketAddress(this.port));
			
			Thread th = new Thread(new ThreadTCPListener());
			th.start();
			
		} catch (IOException e) {
			throw new IOException("TCPConnection:" + e);
		}
		
	}
	
	public class ThreadTCPListener implements Runnable
	{

		public void run() {
			// TODO Auto-generated method stub
			while (!stop)
			{
				try {
					// La synchronisation ne semble pas n�cessaire ici !
					//synchronized(this)
					{
						Socket clientConnected = s.accept();
						
						if (delegate != null)
							delegate.receivedConnexion(clientConnected);
						
						// le socket est coupé au niveau du délégué,pas de problème ! 
					}
					
				} catch (IOException e) {
					// Aie gros probl�me !!!!
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
