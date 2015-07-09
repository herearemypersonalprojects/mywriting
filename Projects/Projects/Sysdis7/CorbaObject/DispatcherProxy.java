package CorbaObject;

import CorbaGeneric.Server;
import CorbaObject.DispatcherPackage.Informations;

public class DispatcherProxy implements DispatcherOperations {

	// L'ior de l'objet vis� !
	public String IOR;
	private CorbaObject.Dispatcher disp =null;
	
	public DispatcherProxy(String newCorbaObject) {
		// TODO Auto-generated constructor stub
		this.IOR = newCorbaObject;
		
	}

	
	private void init()
	{
		if (this.disp == null)
		{
			org.omg.CORBA.Object obj = Server.orb.string_to_object(this.IOR);
			this.disp = DispatcherHelper.narrow(obj);
		}
	}
	
	public int commanderUnRendu(byte[] contentpovrayfile, int width, int height) {
		// TODO Auto-generated method stub
		this.init();
		return this.disp.commanderUnRendu(contentpovrayfile, width, height);
	}

	public Informations obtenirEtatAvancement(int numeroticket) {
		// TODO Auto-generated method stub
		this.init();
		return this.disp.obtenirEtatAvancement(numeroticket);
	}

	public void updateFragment(int numticket, int numfragment, byte[] content) {
		// TODO Auto-generated method stub
		this.init();
		this.disp.updateFragment(numticket, numfragment, content);
	}


	public byte[] obtenirResultatFinal(int numticket) {
		// TODO Auto-generated method stub
		this.init();
		return this.disp.obtenirResultatFinal(numticket);
	}


	public boolean annulerTache(int numticket) {
		// TODO Auto-generated method stub
		this.init();
		return this.disp.annulerTache(numticket);
	}


	public void setID(int IDnumber) {
		// TODO Auto-generated method stub
		this.init();
		// permet de spécifier le numéro du dispatcher !
		this.disp.setID(IDnumber);
	}


	public void saveNewClient(String IOR) {
		// TODO Auto-generated method stub
		this.init();
		this.disp.saveNewClient(IOR);
	}


	public void saveNewDispatcher(String IOR) {
		// TODO Auto-generated method stub
		this.init();
		this.disp.saveNewDispatcher(IOR);
	}


	public void saveNewWorker(String IOR) {
		// TODO Auto-generated method stub
		this.init();
		this.disp.saveNewWorker(IOR);
	}


	public int synchronizeCommandeDeRendu(byte[] contentpovrayfile, int width, int height, int numberticket, int datedebuttache, int heuredebuttache, int datefin, int heurefin) {
		// TODO Auto-generated method stub
		this.init();
		return this.disp.synchronizeCommandeDeRendu(contentpovrayfile, width, height, numberticket, datedebuttache, heuredebuttache, datefin, heurefin);

	}


	public void dearLeaderAreUStillThere() {
		// TODO Auto-generated method stub
		this.init();
		this.disp.dearLeaderAreUStillThere();
	}

	

}
