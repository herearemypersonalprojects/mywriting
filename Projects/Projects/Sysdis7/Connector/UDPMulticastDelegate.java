package Connector;

import java.net.DatagramPacket;

public interface UDPMulticastDelegate {

	// Delegate concernant le reçu de packet udp...
	

	public void receivedData(UDPString message, DatagramPacket packet);
	
}
