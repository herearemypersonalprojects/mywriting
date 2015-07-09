import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.CharBuffer;

import CorbaObject.DispatcherPackage.Informations;
import Structure.Client;
import Structure.Timer;
import Structure.Utils;


public class client {

	public static void main(String[] args) {
		
		// on doit lire le contenu du fichier pass� en argument !
		
		byte[] contenttodo = null;
		
		Integer width=null;
		Integer height = null;
		
		try
		{
			String filename = args[0].toString();
			
			contenttodo = Utils.getContentOfFile(filename);
			
			width = new Integer (args[1].toString());
			height = new Integer (args[2].toString());
		
		}
		catch(Exception exc)
	    {
			// le fichier n'a pas �t� trouv� !
			System.out.println("Erreur");
			System.exit(-1);
	    }
		//System.out.println("� traiter : " + contenttodo);
		Client cli = new Client();
		int ticket;
		
		
		
		System.out.println("ticket de la commande : " + (ticket = cli.getDispatcherLeader().commanderUnRendu(contenttodo, width.intValue(), height.intValue())));
		
		
		Informations info = cli.getDispatcherLeader().obtenirEtatAvancement(ticket);
		while (info == null)
		{
			new Timer(500); // on fait une mini pause !
			info = cli.getDispatcherLeader().obtenirEtatAvancement(ticket);
		}
		while (info != null && info.percentageDone != 100)
		{
			//System.out.println("avancement du ticket : " + ticket + " @ " + info.percentageDone);
			new Timer (2000);
			info = cli.getDispatcherLeader().obtenirEtatAvancement(ticket);
		}
		System.out.println("tache numero : " + ticket + " terminee en " + (info.endingTime - info.startingTime) + " seconde(s)");
		

		
		cli.close();
	}
	
}
