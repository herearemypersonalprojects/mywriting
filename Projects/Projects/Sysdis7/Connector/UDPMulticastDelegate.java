package Connector;

import java.net.DatagramPacket;

public interface UDPMulticastDelegate {

	// Delegate concernant le re�u de packet udp...
	

	public void receivedData(UDPString message, DatagramPacket packet);
	
}
