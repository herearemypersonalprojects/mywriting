package CorbaObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import CorbaObject.DispatcherPackage.Informations;
import Structure.DATA;
import Structure.Logger;
import Structure.Timer;
import Structure.Utils;

public class DispatcherImplemented extends DispatcherPOA{
	
	// Le logger permettant l'affichage à l'écran des différents messages !
	private Logger log = Logger.getLogger(this.getClass());
	
	// variable utilisée permettant de référencé la dernière tâche soumisse. 
	// grâce à celle-ci, les nouveaux workers pourront directement commencer cette tâche !
	// ce concept est donc lié avec le principe de "fast starting"
	private task lastTaskWorking=null;
	
	// soit un lien vers les stratégies UPD & TCP du dispatcher...
	// le fait de différencier les deux niveaux permet de créer un code indépendant niveau services !
	private Structure.Dispatcher dispatcher;
	
	// soit la liste des tâches enregistrées au sein d'un dispatcher
	private ArrayList task = new ArrayList();
	
	// Le constructeur, il prend en argument l'objet encapsulant les stratégies de connexions, élections,...
	public DispatcherImplemented(Structure.Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		this.log.info("CORBA Dispatcher created");
	}

	// Méthode CORBA directement appelée afin de passer un rendu auprès du dispatcher,
	// est donc renvoyé le numéro du ticket...
	public int commanderUnRendu(byte[] contentpovrayfile, int width, int height) {
		
		// TODO Auto-generated method stub
		// ici le code est nécessaire pour créer la nouvelle tâche, dans
		// la synchronisation, il faut ajouter les inforamtions du ticket, etc...
		
		int epaisseurDecoupe = DATA.CHUNKHEIGHT;
		
		log.info("CORBA Dispatcher leader chunk height chosen : " + epaisseurDecoupe );
		task ta = new task(contentpovrayfile, width, height, this.task.size(), ((int) Math.ceil( new Float(height).floatValue() / new Float(epaisseurDecoupe).floatValue())), epaisseurDecoupe);
		
		// là on envoie la tache à tous les dispatchers, histoire de synchronizer les commandes !
		
		// on doit enregistrer ce worker auprès des autres dispatchers !
		dispatcher.logStructure.info("Dispatcher : " + this.dispatcher.ID + " new command synchronized ;");
		
		// ça c'est nécessaire !, il faut évidemment enregistrer la tâche auprès des autres dispatchers, mais, vu que
		// maintenant, nous avons spécifié d'autres informations, comme le numéro de la tâche, nous les passons au travers d'arguments...
		for (int j=0; j < this.dispatcher.dispatchers.size()  && dispatcher.Iamtheleader ; j +=1)
		{
			try
			{
				DispatcherProxy prox = (DispatcherProxy) this.dispatcher.dispatchers.get(j);
				if (prox != null)
				{
					prox.synchronizeCommandeDeRendu(contentpovrayfile, width, height, ta.getTicket(), ta.startingDate, ta.startingTime, ta.endingDate, ta.endingTime);
				}
			}
			catch (Exception e)
			{
				// can't access the proxydispatcher, setting it to null !
				// Remarque : ce genre de construction est énormément utilisé dans le projet.
				// effectivement, vu que le programme est massivement threadé (plusieurs dizaines de threads voir peut être +100)
				// une boucle for sur des éléments d'un tableau de taille CROISSANTE (il n'y a jamais de remove()), 
				// ne peut pas faire tomber le système.
				// même si lors du traitement la taille du vecteur change, la ré-évaluation de la condition dans le for
				// en prend également compte !
				// évidemment, si, lors de l'appel CORBA, un problème se pose, on désactive simplement le proxy en le remplaçant par un objet null
				this.dispatcher.dispatchers.set(j, null);
			}
		}
		
		// ajout de la nouvelle tâche dans la liste des tâches !
		this.task.add(ta);
		
		// on lance le do the work sur cette tache pour etre ser qu'on la traite jusqu'au bout !
		// le "do the work" est en fait une boucle excécutée constamment sur une tâche jusqu'à celle-ci soit terminée
		// elle est donc relancée après un certain nombre de secondes...
		Thread th = new Thread(new doTheWork(ta));
		th.start();
		
		// retour du ticket au client;
		return ta.getTicket();
		
	}

	public Informations obtenirEtatAvancement(int numeroticket) {
		// TODO Auto-generated method stub
		// il faut d'abord obtenir la teche correspondant au ticket !
		
		Iterator it = this.task.iterator(); // soit les taches
		boolean found = false;
		task ta = null;
		while (it.hasNext() && !found)
		{
			ta =  (task) it.next(); // soit une tache,
			if (ta != null && ta.getTicket() == numeroticket)
			{
				found = true;
			}
		}
		
		if (found)
		{
			// la tâche correspondante est trouvee !
			// ta correspond à cette tache !
			// si les valeurs 0 sont renvoyees, cela signifie que l'evenement ne s'est pas encore presente !
			return new Informations(ta.getPercentageDone(),ta.startingTime,ta.endingTime, ta.startingDate, ta.endingDate);
			
		}
		else
			return null; // si la teche n'a pas ete trouvee, alors on retourne une valeure nulle...
		// c'est plus propre que de jeter une exception !
	}

	public void updateFragment(int numticket, int numfragment, byte[] content) {
		// TODO Auto-generated method stub
		
		Iterator it = this.task.iterator(); // soit les taches
		boolean found = false;
		task ta = null;
		while (it.hasNext() && !found)
		{
			ta =  (task) it.next(); // soit une tache,
			if (ta != null && ta.getTicket() == numticket)
			{
				found = true;
			}
		}
		
		if (found)
		{
			// la teche correspondante est trouvee !
			if (!ta.isFinished())
			
				ta.updateChunkDone(numfragment, content);
			
		}
		
		// on peut également prévenir les autres dispatchers du fragment reçu !
		// évidemment, uniquement le leader fait cela !
		// on pourrait même également threader l'appel, mais là, ça fait un peu beaucoup !
		for (int j=0; j < this.dispatcher.dispatchers.size() && dispatcher.Iamtheleader ; j +=1)
		{
			try
			{
				DispatcherProxy prox = (DispatcherProxy) this.dispatcher.dispatchers.get(j);
				if (prox != null)
				{
					prox.updateFragment(numticket, numfragment, content);
				}
			}
			catch (Exception e)
			{
				// can't access the proxydispatcher, setting it to null !
				this.dispatcher.dispatchers.set(j, null);
			}
		}
	}
	
	// Cette inner classe permet de stocker l'information concernant les tâches.
	public class task
	{
		// Creation d'une image afin de construire l'image finale !
		private BufferedImage MyImage;
		private byte[] contentpovrayfile;
		private int width;
		private int height;
		private int ticket;
		private boolean[] chunkAlreadyDone;
		private int chunkHeight;
		private int numberoffragment;
		private boolean alreadyStarted= false;
		private int numberoffragmentrendered = 0;
		private int startingTime = 0; 
		private int endingTime = 0;
		private int startingDate=0;
		private int endingDate = 0;
		private String finalefilename;
		
		public int getPercentageDone()
		{
			if(this.endingDate != 0 && this.endingTime != 0) return 100;
			// le if suivant n'est qu'un mesure de precautions si appels concurrents !
			if (numberoffragmentrendered > numberoffragment) numberoffragmentrendered = numberoffragment;  
			return ((100 *numberoffragmentrendered) / this.numberoffragment);
		}
		
		public task(byte[] contentpovrayfile, int width, int height, int ticket, int numberoffragment, int epaisseurDecoupe)
		{
			// Il faut verifier : width, height, ticket, numberoffragment;
			if (numberoffragment <= 0) numberoffragment = 1;
			
			this.chunkHeight = epaisseurDecoupe;
			this.contentpovrayfile = contentpovrayfile;
			this.width = width;
			this.height = height;
			this.ticket = ticket;
			this.chunkAlreadyDone = new boolean[numberoffragment]; // au debut on a encore rien fait !
			for (int i=0; i < this.chunkAlreadyDone.length; i+=1)
			{
				this.chunkAlreadyDone[i] = false;
			}
			this.numberoffragment = numberoffragment;
			
			// creation de l'image finale !
			this.MyImage = new BufferedImage(width, height, 5); // image avec les tailles finales !
			
		}
		public void updateChunkDone(int chunknumber, byte[] content)
		{
			
			if (this.startingDate == 0) this.startingDate = new Integer(Utils.getCalendarDate()).intValue();
			if (this.startingTime == 0) this.startingTime = new Integer(Utils.getCalendarTime()).intValue();
			// on regarde s'il existe :
			
			if (!this.chunkAlreadyDone[chunknumber])
			{
				this.chunkAlreadyDone[chunknumber] = true;
				
				
			this.numberoffragmentrendered += 1;	
			// on imprime rapidement ce qui a deje ete fait !
			
			/*
			 * 
			 * Nous pouvons presenter cela pour la demo
			 * 
			 */	
				
			//Utils.printScreenWhatDone(chunkAlreadyDone);
			String messagesup= "";
			if (!dispatcher.Iamtheleader)
				messagesup += " But I'm not leader " + dispatcher.ID;
			log.debug(Utils.getPrintScreenWhatDone(chunkAlreadyDone) + messagesup);
			
			/*
			 * 
			 * Mais les performances en patissent !
			 * 
			 * 
			 */	
				
			// de plus on previent les workers !
			
			for (int i=0;i < dispatcher.workers.size(); i+=1)
			{
				if (dispatcher.workers.get(i) != null)
				{
					WorkerProxy wopoxy = (WorkerProxy) dispatcher.workers.get(i);
					Thread th = new Thread(new sendMessageUpdated(wopoxy, i, this.getTicket(), chunknumber));
					th.start();
				}
				
			}
			
			// on pense à gentillement copier le contenu produit dans un fichier, la classe Utils est pratique pour cela !
			
			String filenamecool ="dispatcherLeader-"+dispatcher.ID+"-task" + this.getTicket() + "-fragment" + chunknumber + ".png"; 
			
			Utils.writeContentToFile(content, filenamecool);
			
			// il faut egalement reconstituer l'image bientot finale !
			/*
			 * 
			 * 
			 * Le merger ci-contre est fait main...
			 * 
			 * 
			 */
			
			BufferedImage image = null;
			try 
			{
				image = ImageIO.read(new File(filenamecool));
			} 
			catch (IOException e1) 
			{
				log.error("Error while reading fragment", e1);
			}
			
			// le premier argument = le debut en x
			// le deuxieme argument = le debut en y
			try
			{
				// soit un tableau de pixels
				int[] RGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
				// qui va directement dans l'image... (à l'emplacement voulu évidemment !)
				this.MyImage.setRGB(0, chunknumber * this.getChunkHeight(), image.getWidth(), image.getHeight(), RGB, 0, this.MyImage.getWidth());
			
			}
			catch (Exception e)
			{
				log.error("Oups, problem when adding the new picture !", e);
				
				// on ne fait rien ici... ce n'est pas important, c'est seulement lorsque povray n'est pas là !
				// enfin, en théorie ;-)
			}
			
			
			// Nous vérifions où en est la tâche,
			// ça pourrait être bien si l'on pouvait calculer les différentes périodes,
			// de plus, nous pouvons créer l'image finale et même forcer les workers à s'arrêter
			// Ceci est évidemment plus une mesure de sécurité qu'autre chose !
			if (this.isFinished())
			{
				// Si la tache est terminee, on le dit 
				
				if (this.endingTime == 0) this.endingTime = new Integer(Utils.getCalendarTime()).intValue();
				if (this.endingDate == 0) this.endingDate = new Integer(Utils.getCalendarDate()).intValue();
				
				log.info("CORBA Dispatcher leader task number : " + this.getTicket() + " started @ : " + this.startingDate + ":" + this.startingTime + " ended @ : " + this.endingDate + ";" + this.endingTime);
				log.info("CORBA Dispatcher task number : " + this.getTicket() + " takes : " + (this.endingTime - this.startingTime) + " seconds");
				
				// on pense à demander à tous les workers d'arreter de travailler pour cette tâche !
				if (dispatcher.Iamtheleader) // on pense évidemment à arrêter les worker si l'on est leader !
					stopWorkersWorkingOn(this.getTicket());
				
				// on doit ecrire le fichier final de la tâche !
				
				File finale = null;
				try
				{
					this.finalefilename = "dispatcherLeader-" + dispatcher.ID + "-task" + this.getTicket() + "-final.png";
					finale = new File(finalefilename);
				}
				catch (Exception e)
				{
					log.error("Can't create the latest picture file", e);
				}
				
				try 
				{
					ImageIO.write(this.MyImage, "png", finale);
				} 
				catch (IOException e) 
				{
					log.error("Can't write the latest picture file", e);
					e.printStackTrace();
				}
				
			}
			
			}
		}
		
		public class sendMessageUpdated implements Runnable
		{
			private int numberinarray;
			private WorkerProxy wo;
			private int ticket;
			private int chunknumber;
			
			public sendMessageUpdated(WorkerProxy wo, int numberinarray, int ticket, int chunknumber)
			{
				this.ticket = ticket;
				this.chunknumber = chunknumber;
				this.wo = wo;
				this.numberinarray = numberinarray;
			}

			public void run() {
				
				try
				{
					this.wo.updateJob(this.ticket, this.chunknumber);
				}
				catch (Exception e)
				{
					// oups, probleme avec le worker wopoxy !, il faut le supprimer de la liste
					//System.out.println("oups, probleme avec worker : " + this.wo);
					log.error("Connection lost with the worker : " + wo + " with the ID : " + this.numberinarray);
					
					dispatcher.workers.set(this.numberinarray, null);
				}
			}
		}
		
		public int getChunkHeight() {return this.chunkHeight;}
		public byte[] getContentPovrayFile() {return this.contentpovrayfile;}
		public int getTicket(){return this.ticket;}
		public int getWidth(){return this.width;}
		public int getHeight(){return this.height;}
		public int getNumberOfFragment(){return this.numberoffragment;}
		public void setStartTask()
		{
			// Cette methode n'est reellement executee qu'une seule fois !
			// permet de prendre l'heure e laquelle la tache a ete attribuee...
			if (!this.alreadyStarted)
			{
				this.alreadyStarted = true;
				//System.out.println("tache " + this.getTicket() + " demarree");
				this.startingDate = new Integer(Utils.getCalendarDate()).intValue();
				this.startingTime = new Integer(Utils.getCalendarTime()).intValue();
				log.info("CORBA Dispatcher leader starting new task with ticket : " + this.getTicket() + " @ : " + this.startingDate + ";" + this.startingTime);
				
				//System.out.println();
			}
		}
		
		public int[] getChunkAlreadyDone() 
		{
			int number = 0;
			for (int i=0; i < this.chunkAlreadyDone.length; i+=1)
			{
				if (this.chunkAlreadyDone[i]) number +=1;
			}
			int[] answer = new int[number];
			
			int j=0;
			for (int i=0; i < this.chunkAlreadyDone.length; i+=1)
			{
				if (this.chunkAlreadyDone[i])
				{
					answer[j++] = i;
				}
			}
			return answer;
		}
		
		public boolean isFinished()
		{
			int number = 0;
			for (int i=0; i < this.chunkAlreadyDone.length; i+=1)
			{
				if (this.chunkAlreadyDone[i]) number +=1;
			}
			if (number == this.numberoffragment)
				return true;
			else
				return false;
		}
	}
	
	public class doTheWork implements Runnable
	{
		// soit la tache sur laquel opere le travail ;
		private task task;
		
		public doTheWork(task on)
		{
			// on fait toujours le travail sur une tache...
			this.task = on;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			while (!this.task.isFinished())
				// tant que la teche n'est pas finie !
			{
				// uniquement si on est le leader ! ça parraît logique !
				if (dispatcher.Iamtheleader)
				{
				// on dit que la derniere task demande est celle-ci
				lastTaskWorking = this.task;
				// on lance l'execution directement !, c'est important !
				for (int i =0; i< dispatcher.workers.size(); i+=1)
				{
					// soit tous les workers
					if (dispatcher.workers.get(i) != null)
					{
						WorkerProxy wo = null;
						wo = (WorkerProxy) dispatcher.workers.get(i);
						// on lance un thread par communication pour tout lancer d'un coup !
					
						Thread th = new Thread(new Work(wo, this.task, i));
						th.start();
					}
					
					
					
				}
				
				}
				new Timer(DATA.TIMEBEFOREREASKINGCOMPUTINGTASK); // on fait une mini pause, mais pas trop longtemps !
				
			}
		}
		// Assure que le travail sera fait, le principe est simple,
		// pour chaque worker on s'assure qu'il travaille, si oui, on ne dit rien,
		// sinon on lui donne une teche...
		
	}
	
	public class Work implements Runnable
	{

		private WorkerProxy wo;
		private task ta;
		private int numberInArrayList;
		
		public Work(WorkerProxy wo, task ta, int numberInArrayList)
		{
			this.wo = wo;
			this.ta = ta;
			this.numberInArrayList = numberInArrayList;
		}
		
		public void run() {
			try
			{
				if (dispatcher.Iamtheleader)
				{
				
					if (!wo.areUstilWorking())
					{
						// si le worker ne travaille pas ;
						wo.executeJob(this.ta.getContentPovrayFile(), this.ta.getChunkAlreadyDone(), this.ta.getChunkHeight(), this.ta.getWidth(), this.ta.getHeight(), this.ta.getTicket(), this.ta.getNumberOfFragment());
						// declence le debut du rendement !
						this.ta.setStartTask();
					}
				}
			}
			catch (Exception e)
			{
				// oups, probleme avec le worker ! il faut le supprimer de l'arraylist
				//System.out.println("oups, probleme avec le worker : " + wo);
				log.error("Connection lost with the worker : " + wo + " with the ID : " + this.numberInArrayList);
				
				dispatcher.workers.set(this.numberInArrayList, null);
			}
			
		}}
	
	
	public void fastStarting(WorkerProxy wo, int numberinarray)
	{
		if (lastTaskWorking != null)
		{
			if (!lastTaskWorking.isFinished())
			{
				// alors, ok, on peut demander e ce que le tout nouveau worker y travaille !
				//System.out.println("fast starting for worker : " + numberinarray);
				log.info("CORBA Dispatcher leader fast starting for worker with ID : " + numberinarray);
				
				Thread th = new Thread(new Work(wo, lastTaskWorking, numberinarray));
				th.start();
			}
		}
	}
	
//	 de plus on previent les workers !
	private void stopWorkersWorkingOn(int ticketNumber)
	{
	for (int i=0;i < dispatcher.workers.size(); i+=1)
	{
		if (dispatcher.workers.get(i) != null)
		{
			WorkerProxy wopoxy = (WorkerProxy) dispatcher.workers.get(i);
			try
			{
			wopoxy.cancelTask(ticketNumber);
			}
			catch (Exception e)
			{
				// oups, probleme avec le worker i
				//System.out.println("oups, probleme avec le worker : " + wopoxy);
				log.error("Connection lost with the worker : " + wopoxy + " with the ID : " + i);
				
				dispatcher.workers.set(i, null);
			}
		}
		
	}
	}

	public byte[] obtenirResultatFinal(int numticket) {
		// d'abord il faut chercher le ticket,
// il faut d'abord obtenir la teche correspondant au ticket !
	
		//System.out.println("on voudrait le ticket num " + numticket);
		log.info("Client asked for the result of task number : " + numticket);
		Iterator it = this.task.iterator(); // soit les taches
		boolean found = false;
		task ta = null;
		while (it.hasNext() && !found)
		{
			ta =  (task) it.next(); // soit une tache,
			if (ta.getTicket() == numticket);
			{
				found = true;
			}
		}
		
		if (found)
		{
			// ajouter un peu de traitement pour obtenir la tache finale
			if (ta.isFinished())
			{
				// il faut que la tache soit finie pour envoyer le resultat !
				
				return Utils.getContentOfFile("dispatcherLeader-" + dispatcher.ID + "-task" + numticket + "-final.png");
			}
			else
				return null; // pour l'instant on a rien fait le !
				
		}
		else
			return null;// si le ticket ne correspond e rien !
	}

	public boolean annulerTache(int numticket) {
		// il faut d'abord trouver la tache associee au ticket
		Iterator it = this.task.iterator(); // soit les taches
		boolean found = false;
		task ta = null;
		while (it.hasNext() && !found)
		{
			ta =  (task) it.next(); // soit une tache,
			if (ta.getTicket() == numticket);
			{
				found = true;
			}
		}
		
		if (found)
		{
			// doit inserer un peu de traitement ! 
			return true; // pour l'instant on a rien fait le !
		}
		else
			return false;
	}

	public void setID(int IDnumber) {
		// TODO Auto-generated method stub
		this.dispatcher.ID = IDnumber;
		this.log.info("DISPATCHER IMPLEMENTED receiving new ID : " + IDnumber);
		
		// Permet de passer le trou... => les id seront croissant comme ça !
		while (dispatcher.dispatchers.size() < IDnumber)
			dispatcher.dispatchers.add(null);
		
	}

	
	// permet de tenir à jour les éléments du système !
	public void saveNewClient(String IOR) {
		
//		 TODO Auto-generated method stub
		dispatcher.logStructure.info("Dispatcher : " + this.dispatcher.ID + " Saving new client ;");
		// on doit enregistrer ce worker auprès des autres dispatchers !
		for (int j=0; j < this.dispatcher.dispatchers.size() && this.dispatcher.Iamtheleader; j +=1)
		{
			try
			{
				DispatcherProxy prox = (DispatcherProxy) this.dispatcher.dispatchers.get(j);
				if (prox != null)
				{
					prox.saveNewClient(IOR);
				}
			}
			catch (Exception e)
			{
				// can't access the proxydispatcher, setting it to null !
				this.dispatcher.dispatchers.set(j, null);
			}
		}
		// TODO Auto-generated method stub
		// avant d'ajouter un nouveau proxy, on regarde si il n'est pas déjà présent !
		// cela peut paraître "de trop", mais c'est plus sûr !
		boolean found = false; 
		for (int i=0; i < this.dispatcher.clients.size() && !found; i +=1)
		{
			ClientProxy prox = (ClientProxy) this.dispatcher.clients.get(i);
			if (prox != null)
			{
				if (prox.IOR.equals(IOR))
					found = true;
			}
		}
		if (!found) // ajout s'il n'a pas été trouvé !
			dispatcher.clients.add(new ClientProxy(IOR));
		
	}

	public void saveNewDispatcher(String IOR) {
		// TODO Auto-generated method stub

		// on peut s'ajouter soi même mais il ne faut pas que l'on soit accessible !
		{
			// d'abord, on cherche après !
			boolean found = false;
			for (int i=0 ; i < this.dispatcher.dispatchers.size() && !found; i+=1)
			{
				DispatcherProxy prox = (DispatcherProxy) this.dispatcher.dispatchers.get(i);
				if (prox != null)
				{
					if (prox.IOR.equals(IOR)) found = true;
				}
			}
			if (!found)
			{
				// on essaie de l'insérer !
				DispatcherProxy prox = null;
				
				// si c'est soi même on ne crée qu'un objet null, comme cela, on évite de s'adresser soi même !
				
				if (!this.dispatcher.IOR.equals(IOR))
				{	
					prox = new DispatcherProxy(IOR);
					dispatcher.logStructure.info("Dispatcher : " + this.dispatcher.ID + " Saving new dispatcher ;");
				}
				
				// le truc ici est de donner des identifiants de plus en plus grands...
				
				/*
				int pos = idfordispatcher;
				idfordispatcher += 1;
				*/
				// là c'est l'ancienne méthode !
				int pos = dispatcher.dispatchers.size();
				
				// 
				
				try
				{
					
				
				
				// avec ce proxy, on essai de mettre à jour ses connaissances des tâches !
				for (int z = 0; z < task.size(); z+=1)
				{
					task ta = (task) task.get(z);
					prox.synchronizeCommandeDeRendu(ta.getContentPovrayFile(), ta.getWidth(), ta.getHeight(), ta.getTicket(), ta.startingDate, ta.startingTime, ta.endingDate, ta.endingTime);
					// de plus pour chacune des taches, il faut envoyer le fragment correspondant !
					
				}
				// également de la présence des workers;
				for (int z=0; z< dispatcher.workers.size() ; z +=1)
				{
					WorkerProxy po = (WorkerProxy) dispatcher.workers.get(z);
					if (po != null)
					{
						prox.saveNewWorker(po.IOR);
					}
				}
				// également de la présence des dispatchers;
				for (int z=0; z < dispatcher.dispatchers.size() && this.dispatcher.Iamtheleader ; z += 1)
				{
					DispatcherProxy po = (DispatcherProxy) dispatcher.dispatchers.get(z);
					if (po != null)
					{
						// ok c'est bien !
						prox.saveNewDispatcher(po.IOR);
					}
				}
				// on place le dispatcher une fois la synchronisation assez avancée !
				dispatcher.dispatchers.add(prox);
				
				// de plus les autres dispatchers doivent connaître cette nouvelle présence !
				for (int z=0; z < dispatcher.dispatchers.size() && this.dispatcher.Iamtheleader; z += 1)
				{
					DispatcherProxy po = (DispatcherProxy) dispatcher.dispatchers.get(z);
					if (po != null)
					{
						try
						{
							po.saveNewDispatcher(prox.IOR);
						}
						catch (Exception e)
						{
							dispatcher.dispatchers.set(z, null);
						}
					}
				}
				
				
				if (dispatcher.Iamtheleader)
					if (dispatcher.ID != -1)
					{
						log.info("Dispatcher with ID : " + dispatcher.ID + " is sending message to setID to new recorded dispatcher : setID(" + (pos+1) +")");
						prox.setID(pos+1); // uniquement après avoir fait la mise à jour des données on l'élève au stade de pseudo dispatcher.
					}
				// là on essaie d'aller plus loin !
				
				for (int z = 0; z < task.size() && this.dispatcher.Iamtheleader; z+=1)
				{
					task ta = (task) task.get(z);
					
					/* on thread à partir d'ici ! */
					Thread thdan = new Thread(new synchronizeChunk(ta, prox));
					thdan.start();
					/* on s'arrête ici pour threader ! */
				}
				
				
				}
				catch (Exception e)
				{
					boolean settingnull = false;
					while (!settingnull)
					{
					try{
						dispatcher.dispatchers.set(pos, null);
						settingnull = true;
					}
					catch(Exception e1)
					{
						dispatcher.dispatchers.add(null);
						settingnull = false;
					}
					}
				}
			}
		}
			
	}
	
	public class synchronizeChunk implements Runnable
	{
		// cette classe à pour seul but d'envoyer l'ensemble des fragements
		// d'une tâche à un dispatcher afin de le synchroniser !
		
		private task ta;
		private DispatcherProxy prox;

		public synchronizeChunk(task ta, DispatcherProxy prox)
		{
			this.ta = ta;
			this.prox = prox;
		}
		

		public void run() {
			try
			{
			// TODO Auto-generated method stub
			for (int a=0; prox != null && ta != null && a<ta.numberoffragment; a+=1)
			{
				if (ta.chunkAlreadyDone[a])
				{
					// le fragment a été rendu, alors, on l'envoie...
					String filenamecool ="dispatcherLeader-"+dispatcher.ID+"-task" + ta.getTicket() + "-fragment" + a + ".png"; 
					
					prox.updateFragment(ta.getTicket(), a, Utils.getContentOfFile(filenamecool));
				}
			}
			}
			catch(Exception e)
			{
				// ce n'est rien !
				// cela signifie que la synchronisation des fragments n'est pas parfaite !
				// de toute façon, il y a deux choix;
				// soit le nouveau dispatcher à synchroniser vient de mourrir !
				// soit c'est autre chose, et de toute façon le dispatcher redemandera aux workers de travailler pour lui !
			}
		}
		
	}

	public void saveNewWorker(String IOR) {
		// TODO Auto-generated method stub
		// on doit enregistrer ce worker auprès des autres dispatchers !
		for (int j=0; j < this.dispatcher.dispatchers.size() && this.dispatcher.Iamtheleader; j +=1)
		{
			try
			{
				DispatcherProxy prox = (DispatcherProxy) this.dispatcher.dispatchers.get(j);
				if (prox != null)
				{
					prox.saveNewWorker(IOR);
				}
			}
			catch (Exception e)
			{
				// can't access the proxydispatcher, setting it to null !
				this.dispatcher.dispatchers.set(j, null);
			}
		}
		
		boolean found = false;
		for (int i =0;i < this.dispatcher.workers.size() && !found ; i+=1)
		{
			WorkerProxy prox = (WorkerProxy) this.dispatcher.workers.get(i);
			if (prox != null)
			{
				if (prox.IOR.equals(IOR)) found = true;
			}
		}
		if (!found)
			{
			dispatcher.logStructure.info("Dispatcher : " + this.dispatcher.ID + " Saving new worker ;");
			
			// On cree le proxy de l'objet corba et on l'ajoute e la liste des workers
			// vu qu'il est possible qu'il y ait des trous dans la liste des workers, on essaie d'abord de les combles;
			// cela permet egalement d'attribuer correctement les identifiants aux workers !
			WorkerProxy wopoxy  = new WorkerProxy(IOR);
			
			boolean inserted = false;
			
			 //System.out.println("received connection to add worker !");
			//logStructure.info("TCPSERVER was waiting for worker", e);
			
			for (int i=0 ; i<dispatcher.workers.size() && !inserted; i+=1)
			{
				if (dispatcher.workers.get(i) == null)
				{
					inserted = true;
					dispatcher.workers.set(i, wopoxy);
					try
					{
						if (dispatcher.Iamtheleader)
							wopoxy.setId(i);
						
						dispatcher.getInstance().fastStarting(wopoxy, i);
						//System.out.println("worker remplacant un trou ajoute");
						dispatcher.logStructure.info("TCPSERVER has connected new worker by remplacing a no responding one");
					}
					catch (Exception e)
					{
						// si probleme, on l'enleve !
						// System.out.println("probleme ajout d'un worker dans un trou");
						dispatcher.logStructure.error("TCPSERVER failed when adding a new worker");
						dispatcher.workers.set(i, null);
					}
				}
			}
			
			// si l'ajout dans un trou n'est pas passe, on doit l'ajouter e la fin !
			if (!inserted)
			{
				// il n'y avait pas de trou, soit on doit l'ajouter e la liste !
				
				int position = dispatcher.workers.size();
				dispatcher.workers.add(wopoxy);
				try
				{
					wopoxy.setId(position); // et oui, c'est le dernier, donc on a son numero identifiant !
					dispatcher.getInstance().fastStarting(wopoxy, position);
					//System.out.println("nouveau worker ajoute");
					dispatcher.logStructure.info("TCPSERVER has connected new worker");
				}
				catch (Exception e)
				{
					// si probleme, on l'enleve !
					// System.out.println("probleme ajout d'un tout nouveau worker !");
					dispatcher.logStructure.error("TCPSERVER failed when adding a new worker");
					dispatcher.workers.set(position, null);
				}
			}
			
			}
	}

	public int synchronizeCommandeDeRendu(byte[] contentpovrayfile, int width, int height, int numberticket, int datedebuttache, int heuredebuttache, int datefin, int heurefin) {
		// TODO Auto-generated method stub
		// soit ici, ça ne compte que quand nous ne sommes pas leader !
		if (!this.dispatcher.Iamtheleader)
		{
			// on doit voir si la commande n'existe pas !
			boolean found = false;
			for (int i=0; i < this.task.size() && !found; i+=1)
			{
				task ta = (task) task.get(i);
				if (ta != null)
				{
					if (ta.getTicket() == numberticket)
						found = true;
				}
			}
			
			if (!found)
			{
				this.log.info("Synchronizing task number " + numberticket);
				// on va devoir l'ajouter !
				boolean added = false;
			
				int epaisseurDecoupe = DATA.CHUNKHEIGHT;
				
				task tasktoinsert = new task(contentpovrayfile, width, height, numberticket, ((int) Math.ceil( new Float(height).floatValue() / new Float(epaisseurDecoupe).floatValue())), epaisseurDecoupe);
				
				tasktoinsert.startingDate = datedebuttache;
				tasktoinsert.startingTime = heuredebuttache;
				tasktoinsert.endingDate = datefin;
				tasktoinsert.endingTime = heurefin;
				while (!added)
				{
					try
					{
						this.task.set(numberticket, tasktoinsert);
						added = true;
					}
					catch (Exception e)
					{
						// ici on n'a pas su ajouter la tâche, il suffit donc d'ajouter des éléments dans la liste !
						this.task.add(null);
						added = false;
					}
					
				}
			}
		}
		
		
		return 0;
	}

	public void dearLeaderAreUStillThere() {
		// TODO Auto-generated method stub
		// c'est bidon, ici on ne fait rien !
		return;
	}
	
}
