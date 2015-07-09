package CorbaObject;

import org.omg.CORBA.IntHolder;

import Structure.Logger;

public class ClientImplemented extends ClientPOA {

	private final Logger log = Logger.getLogger(this.getClass());
	
	
	private Structure.Client client;
	
	public ClientImplemented(Structure.Client client)
	{
		// On sauvegarde la structure liée...
		this.client = client;
		this.log.info("CORBA Worker created");
	}

	public void RenduTermine(int numeroduticket, String contenudelimagefinale) {
		// TODO Auto-generated method stub
		
	}

}
