package CorbaObject;

import CorbaGeneric.Server;

public class ClientProxy implements ClientOperations {

	public String IOR;
	private Client client = null;
	
	public ClientProxy(String newCorbaObject) {
		// TODO Auto-generated constructor stub
		this.IOR = newCorbaObject;
		
	}
	
	private void init()
	{
		if (this.client == null)
		{
		org.omg.CORBA.Object obj = Server.orb.string_to_object(this.IOR);
		this.client = ClientHelper.narrow(obj);
		}
	}

	public void RenduTermine(int numeroduticket, String contenudelimagefinale) {
		// TODO Auto-generated method stub
		this.init();
		this.client.RenduTermine(numeroduticket, contenudelimagefinale);
	}

	
}
