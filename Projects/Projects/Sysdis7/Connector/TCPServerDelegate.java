package Connector;

import java.net.DatagramPacket;
import java.net.Socket;

public interface TCPServerDelegate {

	public void receivedConnexion(Socket sock);
	
}
