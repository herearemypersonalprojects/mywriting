package CorbaObject;

import java.io.BufferedInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import Structure.DATA;
import Structure.Logger;
import Structure.Timer;
import Structure.Utils;

public class WorkerImplemented extends WorkerPOA {
	
	// obtention du log pour la classe courante !
	private final Logger log = Logger.getLogger(this.getClass());
	
	// La classe repr�sente la classe concr�te du worker !
	private int id=0; // par d�faut...
	private boolean connected = false;
	private boolean working = false;
	private int ticket=-1;
	private boolean[] chunkDone;
	
	private boolean forceStop = false; // si un arr�t brutal est demand� !
	
	private Structure.Worker worker;
	private int numberoffragment;
	private String temporaryFileName;
	private int pictureWidth;
	private int pictureHeight;
	private int chunkHeight;
	private Runtime runtime;
	
	private final byte[] bufferTPS = new byte[8];
	
	
	public WorkerImplemented(Structure.Worker worker)
	{
		this.runtime = Runtime.getRuntime();
		this.worker = worker;
		this.log.info("CORBA Worker created");
	}

	public boolean areUstilWorking() {
		// TODO Auto-generated method stub
		
		return this.working;
		
	}

	public void executeJob(byte[] contentpovrayfile, int[] chunkAlreadyDone, int chunkHeight, int width, int height, int ticketnumber, int numberoffragment) {
		// TODO Auto-generated method stub
		// on enregistre les donn�es vitales !
		//this.log.info("CORBA Worker with ID : " + this.id + " receiving new job to do");
		this.forceStop = false;
		this.working = true;
		this.ticket = ticketnumber;
		this.chunkDone = new boolean[numberoffragment];
		this.numberoffragment = numberoffragment;
		this.pictureWidth = width;
		this.pictureHeight = height;
		this.chunkHeight = chunkHeight; // l'�paisseur du d�coupage !
		
		for (int i =0; i < chunkAlreadyDone.length; i+=1)
		{
			
			// on met � jour tout ce qui a d�j� �t� fait !
			this.chunkDone[chunkAlreadyDone[i]]=true;
		}
		
		//System.out.println("woker de id : " + this.id + " recoit le ticket : " + this.ticket);
		log.info("CORBA Worker with ID : " + id + " has received job : " + ticket + " to do");
		// on imprime rapidement ce qui a d�j� �t� fait !
		// ce n'est pas vraiment n�cessaire, mais bon, c'est pour voir !
		//Utils.printScreenWhatDone(this.chunkDone);
			
		
		// le worker doit �crire le fichier qque part...
		// on va l'appeler :
		// worker-id.pov
		// cela est suffisant...
		
		temporaryFileName = "worker-" + this.id + ".pov";
		
		// on �crit le contenu dans un fichier...
		Utils.writeContentToFile(contentpovrayfile, temporaryFileName);
		
		
		// on simule le calcul ; on renvoit seulement son ID ;
		
		// il faut choisir un truc � faire
		Thread th = new Thread (new Work());
		th.start(); // lancement du travail !
		
		
	}
	
	public class Work implements Runnable
	{
		
		public void run() {
			
			int i;
			int pre=0;
			if ((id % 2) == 0)
				{
					i = Utils.quinconceA(id / 2, numberoffragment); // le / 2 est n�cessaire pour prendre tout
				}
			else
				{
					i = Utils.quinconceB((id) /2, numberoffragment); // le / 2 est n�cessaire pour prendre tout !
				}
			
			
			//System.out.println("**********" + id + " " + i);
			
			boolean finished = false; // variable d'arret global 
			boolean first = true; // passer au moins une fois !
			
			while (!finished && !forceStop) // tant qu'il n'a pas fini et qu'on n'a pas essay� de l'arr�ter brutalement !
			{
				
			
			finished = true;
			
			while ((first || !finished) && !forceStop)
			{
				
				// tout ce que le dispatcher leader nous a dit qui �tait fait est valid� !
				if (!chunkDone[i])
				{
					first = false;
					finished = false;
					// System.out.println("worker " + id + " process frag number : " + i);
					
					// c'est le gros bloc qui fait appel � Pov-Ray...
					
					Process process = null;
					
					// i correspond au fragment � traiter !
					// Soit un exemple de traitement povray :
					String filename = temporaryFileName; // soit le fichier contenant notre code !
					
					// le mieux semble de traiter ligne apr�s ligne !
					
					int width = pictureWidth; // on met la largeur
					int height = pictureHeight; // on met la hauteur !
					
					// les donn�es ci-contre �chantillionn�es sont bonnes !
					
					int blockSize = chunkHeight; // on positionne le d�coupage !
					
					int startingRow = (i*chunkHeight) + 1;
					int endingRow = startingRow + blockSize - 1;
					
					StringBuffer datas = null; // l� o� sera plac� la r�ponse !
					
					// on effectue QUE le bout demand� par i...
					{
						
						
						// On v�rifie de ne pas d�passer les colonnes ni les lignes...
						if (endingRow > height) endingRow = height;
						
					// povray +Itest.pov +W480 +H240 +SR1 +ER10 +SC1 +EC10
					String filenamefinal = "worker-" + id + "-" + i + ".png";
					String[] args1 = { "/bin/sh", "-c", "povray +I" + filename + " +W" + width + " +H" + height + " +SR" + startingRow + " +ER" + endingRow + " +SC" + 1 + " +EC" + width + " +O"+ filenamefinal  };
					
					//if (demo) new Timer(1000);
					
					// on peut imprimer la commande, mais cela n'est pas n�cessaire !
					
					String cmd = new String();
					for (int z = 0; z < args1.length; z+=1)
					{
						cmd += args1[z].toString() + " ";
					}
					//System.out.println(cmd);
					
					
					try {
						log.info("CORBA Worker with ID : " + id + " submitting to PovRay : " + cmd);
						process = runtime.exec(args1);
						// la partie suivante est n�cessaire pour attendre la fin de PovRay ! 
						BufferedInputStream stdin = new BufferedInputStream(process.getInputStream());
						// il va falloir lire le contenu de l'inputStream et le placer dans la r�ponse !
						/// non on prendra le fichier g�n�r� � la fin !
						// datas = new StringBuffer(); 

						// on construit le contenu avec un petit buffer de 8ko...
						
						
						//int length = 0;
						
						try {
							while((stdin.read(bufferTPS)) != -1);
						//	{ 
						//		datas.append(new String(buffer).substring(0, length));  
						//	}
						} catch (IOException e) {
							log.error("CORBA Worker with ID : " + id + " error while reading result from PovRay", e);
							throw new RuntimeException("Work.Reading result from povray !:" + e.getMessage() );
							
							
						} 
					
						try {
							stdin.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							log.error("CORBA Worker with ID : " + id + " error while reading result from PovRay", e);
							throw new RuntimeException("Work.Reading result from povray !:" + e.getMessage() );
							
						}
						
						// eventuellement la sortie d'erreur !
						// stderr = new Thread(new ThreadedInputStreamReader(process.getErrorStream()));
						// stderr.start();
					
					} catch (IOException e) {
						// Si il y a une erreur
						log.error("CORBA Worker with ID : " + id + " error while executing the job", e);
						e.printStackTrace();
						
					}
					
					Thread th = new Thread(new sendResult(Utils.getContentOfFile(filenamefinal), i));
					th.start();
					
					} // fin du traitement de povray !
					
					
					
				}
				// ce qu'il serait qu'en m�me bien, c'est que si le chunk choisi a �t� r�alis�,
				//on se force � en prendre un autre tr�s �loign� !
				
				// on pense � trouver l'indice du fragment suivant � calculer !
				
				if ((id % 2) == 0)
					i+=1;
				else
					i-=1;
				
				if (i < 0) i = numberoffragment - 1;
				if (i >= numberoffragment) i = 0;
				
				while (chunkDone[i] && !forceStop) // on retente tout le temps ! 
				{
					// la redistribution faite ici permet de gagner un temps non n�gligeable, 
					// (plusieurs secondes (jusqu'� 30 sec pour une image 6000*6000) lors de lourds traitement)
					// bien que moins na�ve, cette approche n'est pas suffisante pour �tre certain qu'aucun
					// double ne soit calcul�s ! => enfin, c'est d�j� �a !
					pre += 1;
					// et bin mince !, le choisi est d�j� fait !
					// redistribution avec quinconce
					if (((id + pre) % 2) == 0)
					{
						i = Utils.quinconceA((id + pre) / 2, numberoffragment); // le / 2 est n�cessaire pour prendre tout
					}
					else
					{
						i = Utils.quinconceB((id + pre) /2, numberoffragment); // le / 2 est n�cessaire pour prendre tout !
					}
				}
			
			}
			
			}
			
			// on lib�re enfin le worker !
			working = false;
		}
		
	}

	public class sendResult implements Runnable
	{
		private byte[] datas;
		private int number;
		
		public sendResult (byte[] bs, int number)
		{
			this.datas = bs;
			this.number = number;
		}
		
		public void run() {
			{
				boolean send = false;
				boolean allrestarting = false;
				while (!send)
				try
				{
					worker.getDispatcherLeader().updateFragment(ticket, this.number, this.datas);
					send = true;
					
				}
				catch (Exception e)
				{
					send = false;
					
					log.error("CORBA Worker with ID : " + id + " error while transmitting result to leader");
					if (!allrestarting)
					{
						allrestarting = true;
						worker.reset(); // on reset le worker !
					}
					
					new Timer(300); // on fait une petite pause !
				}
				}
			
		}
		
	}
	
	public void updateJob(int numeroticket, int chunkjustdone) {
		// TODO Auto-generated method stub
		if (this.ticket == numeroticket)
		{
			//System.out.println("moi worker " + id + " ne doit plus faire " + chunkjustdone);
			this.chunkDone[chunkjustdone] = true;
			
		}
	}

	public void setId(int id) {
		// TODO Auto-generated method stub
		//System.out.println("worker nouveau id : " + id);
		this.id = id;
	}

	public void wakeUp() {
		this.connected = true;
		//log.info("Waking up worker ! " + id);
		//System.out.println("worker " + id + " wokeup !");
	}
	
	private boolean alreadylaunchedMaintainConnected = false;
	
	public void maintainConnected()
	{
		if (!alreadylaunchedMaintainConnected)
		{
			// a appeler si l'on veut maintenir cet objet connect� (� appel� lorsque la couche tcp � connect� l'ensemble !)
			// évidemment, on s'arrange pour ne l'appeler qu'une seule fois !
			alreadylaunchedMaintainConnected = true;
			Thread th = new Thread(new Reconnect());
			th.start();
		}
	}
	
	public class Reconnect implements Runnable
	{

		public void run() {
			connected = true;
			while (connected)
			{
				connected = false;
				new Timer(DATA.TIMEBEFORERECONNECTING); // il faut qu'en d�ans les qques sec le dispatcher leader l'ai contact� !
				// on laisse toujours environ 5 secondes pour reconnecter !
				
			}
			worker.reset();
		}
		
	}

	public void cancelTask(int ticket) {
		if (this.ticket == ticket)
		{
			log.info("CORBA Worker with ID : " + this.id + " had cancelled its task with ticket : " + ticket);
			//on force l'arr�t du worker !
			this.forceStop = true;
		}
	}
	

}
