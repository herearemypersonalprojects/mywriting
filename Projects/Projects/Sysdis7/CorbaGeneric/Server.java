package CorbaGeneric;

import org.omg.CORBA.*;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

public class Server {
	
	// Permet de placer un nouvel objet Corba sur l'ORB...
	
	public static ORB orb;
	
	public String addCorbaObject(org.omg.PortableServer.Servant tps)
	{
		java.util.Properties props = System.getProperties();
		props.put("org.omg.CORBA.ORBClass", "com.ooc.CORBA.ORB");
		props.put("org.omg.CORBA.ORBSingletonClass", "com.ooc.CORBA.ORBSingleton");
		
		orb = ORB.init(new String[0], props);
		
		
		POA rootPOA = null;
		try {
			rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		} catch (InvalidName e) {
			// ouille ouille !
			e.printStackTrace();
		}
		POAManager manager = rootPOA.the_POAManager();
		
		
		
		
		// on écrit dans un fichier la référence à ce nouvel objet
		String IOR = orb.object_to_string(tps._this_object(orb));
		
		// On valide sa présence ? => mouais !
		try {
			manager.activate();
			Thread newServant = new Thread(new ThreadedCorbaServer());
			newServant.start();
			
		} catch (AdapterInactive e) {
			// ouille ouille !
			e.printStackTrace();
		}
		
		return IOR;
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public class ThreadedCorbaServer implements Runnable
	{
		// lance simplement l'orb spécifié plus haut dans un nouveau thread, comme cela on a le champ libre !
		public void run() {
			orb.run();
		}
		
	}
	
}
