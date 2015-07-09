package Structure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import Connector.TCPServer;
import Connector.TCPServerDelegate;
import Connector.UDPMulticast;
import Connector.UDPMulticastDelegate;
import Connector.UDPString;
import CorbaGeneric.Server;
import CorbaObject.ClientProxy;
import CorbaObject.DispatcherImplemented;
import CorbaObject.DispatcherProxy;
import CorbaObject.WorkerProxy;

public class Dispatcher {

	// Specifie si le dispatcher courant est le leader ou non !
	private UDPMulticast manager;
	public String IOR;
	
	public boolean Iamtheleader = false; // dit si le dispatcher est leader ou non !
	
	public static int ID = -1;
	
	private byte[] b = new byte[1024*1024]; // soit un tampon de 1Mo => utilise dans les echanges TCP
	
	private String LeaderIOR;
	private String LeaderIP;
	
	// On construit les delegates en fonction de l'etat que peut prendre l'objet...
	private final UDPMulticastDelegate delegateWhenLeader = new UDPDelegateWhenLeader();
	private final UDPMulticastDelegate delegateWhenNotConnected = new UDPDelegateWhenNotConnected();
	private final UDPMulticastDelegate delegateWhenDispatcher = new UDPDelegateWhenDispatcher();
	private final TCPServerDelegate serverDelegateDispatcher = new TCPDelegateWhenLeaderForDispatcher();
	private final TCPServerDelegate serverDelegateWorker = new TCPDelegateWhenLeaderForWorker();
	private final TCPServerDelegate serverDelegateClient = new TCPDelegateWhenLeaderForClient();
	
	// Instanciation du log utilise pour la classe de dispatcher structure
	public final Logger logStructure = Structure.Logger.getLogger(this.getClass());
	
	// Liste des objets Corba connus par le dispatcher leader ;
	public Vector workers = new Vector();
	public Vector dispatchers = new Vector();
	public Vector clients = new Vector();
	
	// soit l'instance de l'objet corba;
	private DispatcherImplemented instance;
	
	private DispatcherProxy dispatcherProxy = null;
	
	public Dispatcher()
	{
		// Il faudra creer l'objet CORBA encapsule ici...
		// Je mets e jour l'IOR...
		// 
		this.instance = new DispatcherImplemented(this); 
		this.IOR = new Server().addCorbaObject(instance);
		this.logStructure.info("Servant CORBAObject Dispatcher running");
		
		// puis le on essaie de joindre le systeme !
		// Pour joindre les dispatcher's, on utilise toujours 
		try 
		{
			this.manager = new UDPMulticast(DATA.IPSESSIONMULTICAST, DATA.PORTSESSIONMULTICAST);
			
			// On lance l'aspect comportemental lorsqu'on n'est pas connecte !
			((launchable) this.delegateWhenNotConnected).launch();
			
			// on peut également lancer le thread vérifiant que le leader est toujours présent !
			Thread th = new Thread(new leaderSentinelle());
			th.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.logStructure.error("UDPSESSIONMULTICAST failed", e);
			e.printStackTrace();
			
		}
		
	}
	
	
	
	public class TCPDelegateWhenLeaderForDispatcher implements TCPServerDelegate
	{

		public void receivedConnexion(Socket sock) {
			// fermeture directe de la connexion !
			try {
				// On obtient d'abord l'IOR de l'objet qui se connecte !
				InputStream input = sock.getInputStream();
				int length = input.read(b);
				
				String newCorbaObject = (new String(b)).substring(0, length);
				// eventuellement, on signale la connexion du nouvel objet
				//System.out.println("Nouvel objet CORBA dispatcher :" + newCorbaObject);
				
				// On cree le proxy du nouvel objet corba et on l'ajoute e la liste des dispatchers !
				
				instance.saveNewDispatcher(newCorbaObject); // on essaie de sauvegarder l'objet !
				
				// ensuite, on desire envoyer son IOR afin d'etre joignable
				OutputStream output = sock.getOutputStream();
				output.write(IOR.getBytes());
				// fermeture de la connexion !
				sock.close();
				
				logStructure.info("TCPSERVER has connected new dispatcher");
			} catch (IOException e) {
				// ouille ouille !
				logStructure.error("TCPSERVER was waiting for dispatcher", e);
				e.printStackTrace();
				
			}
		}
		
	}
	
	public class TCPDelegateWhenLeaderForWorker implements TCPServerDelegate
	{

		public void receivedConnexion(Socket sock) {
			// fermeture directe de la connexion !
			try {
				// On obtient d'abord l'IOR de l'objet qui se connecte !
				InputStream input = sock.getInputStream();
				int length = input.read(b);
				
				String newCorbaObject = (new String(b)).substring(0, length);
				// eventuellement, on signale la connexion du nouvel objet
				//System.out.println("Nouvel objet CORBA worker :" + newCorbaObject);
				// On construit un proxy pour le nouveau worker designe comme objet corba !
			
				// On cree le proxy de l'objet corba et on l'ajoute e la liste des workers
				// vu qu'il est possible qu'il y ait des trous dans la liste des workers, on essaie d'abord de les combles;
				// cela permet egalement d'attribuer correctement les identifiants aux workers !
				
				instance.saveNewWorker(newCorbaObject);
				
				// ensuite, on desire envoyer son IOR afin d'etre joignable
				OutputStream output = sock.getOutputStream();
				output.write(IOR.getBytes());
				// fermeture de la connexion !
				sock.close();
			} catch (IOException e) {
				// ouille ouille !
				logStructure.error("TCPSERVER was waiting for worker", e);
				e.printStackTrace();
				
			}
		}
		
	}
	
	public class TCPDelegateWhenLeaderForClient implements TCPServerDelegate
	{

		public void receivedConnexion(Socket sock) {
			// fermeture directe de la connexion !
			try {
				// On obtient d'abord l'IOR de l'objet qui se connecte !
				InputStream input = sock.getInputStream();
				int length = input.read(b);
				
				String newCorbaObject = (new String(b)).substring(0, length);
				// eventuellement, on signale la connexion du nouvel objet
				//System.out.println("Nouvel objet CORBA client :" + newCorbaObject);
				
				// on cree le proxy de l'objet corba, et on l'ajoute e la liste des clients !
				instance.saveNewClient(newCorbaObject);
				
				// ensuite, on desire envoyer son IOR afin d'etre joignable
				OutputStream output = sock.getOutputStream();
				output.write(IOR.getBytes());
				// fermeture de la connexion !
				sock.close();
				logStructure.info("TCPSERVER has connected new client");
			} catch (IOException e) {
				// ouille ouille !
				logStructure.error("TCPSERVER was waiting for client", e);
				e.printStackTrace();
				
			}
		}
		
	}
	public class UDPDelegateWhenLeader implements UDPMulticastDelegate, launchable
	{
		public void launch()
		{
			if(ID == -1)
			{
				ID = 0; // soit l'id est ici on considère évidemment le tout premier !
			}
			
			// on set le delegate toujours en premier !
			manager.setDelegate(delegateWhenLeader);
			manager.delDelegate(delegateWhenNotConnected);
			manager.delDelegate(delegateWhenDispatcher);
			
			//System.out.println("je suis dispatcher leader !");
			
			// Si je suis le leader je dois agir comme serveur TCP afin de pouvoir envoyer mon IOR
			try {
				// Afin d'etre efficace, je vais ouvrir un port pour chaque type d'objet que je peux gerer !
				
				// soit créatoin du premier serveur tcp !
				// attention on s'arrange ici pour que le port soit alloué coute que coute !
				TCPServer server1 = null;
				boolean done1 = false;
				while (!done1 && !Iamtheleader)
				{
					try
					{
						server1 = new TCPServer(DATA.PORTTCPLEADERFROMDISPATCHER);
						done1 = true;
						
						server1.setDelegate(serverDelegateDispatcher);
					}
					catch (Exception e)
					{
						logStructure.error("TCPport seems to be occupied, waiting " + ID);
						done1 = false;
						new Timer(100);
					}
					
				}
				
				
				
				TCPServer server2 = null;
				boolean done2 = false;
				while (!done2  && !Iamtheleader)
				{
					try
					{
						server2 = new TCPServer(DATA.PORTTCPLEADERFROMWORKER);
						done2 = true;
						
						server2.setDelegate(serverDelegateWorker);
						
					}
					catch (Exception e)
					{
						logStructure.error("TCPport seems to be occupied, waiting " + ID);
						done2 = false;
						new Timer(100);
					}
				}
				
				TCPServer server3 = null;
				boolean done3 = false;
				while (!done3  && !Iamtheleader)
				{
					try
					{
						server3 = new TCPServer(DATA.PORTTCPLEADERFROMCLIENT);
						done3 = true;
						
						server3.setDelegate(serverDelegateClient);
						
					}
					catch (Exception e)
					{
						logStructure.error("TCPport seems to be occupied, waiting " + ID);
						done3 = false;
						new Timer(100);
					}
				}
				
				
				// on doit egalement garantir la connexion des elements !
				
				Thread th = new Thread(new MaintainConnected());
				th.start();
				
				Iamtheleader = true; // enfin, je sais que je suis bien constitué pour être le leader !
				
				logStructure.info("UDPDELEGATE I'm the dispatcher leader, waiting for connections");
				
				manager.sendData(DATA.IAMTHEDISPATCHERLEADER);
			
			} catch (Exception e) {
				// ouille gros probleme !
				logStructure.error("UDPDELEGATE Dispatcher leader error when launching the TCP's servers", e);
				//e.printStackTrace();
				
			}
			
			
			
		}
		
		public void receivedData(UDPString message, DatagramPacket packet) {
			// Comportement e adopter lorsque l'on est le dispatcher leader
			if (message.toString().startsWith(DATA.HELLOIAMADISPATCHER.toString())||message.equals(DATA.HELLOIAMACLIENT)||message.equals(DATA.HELLOIAMAWORKER)  )
			{
				// On doit repondre aux composants
				try {
					manager.sendData(DATA.IAMTHEDISPATCHERLEADER);
				} catch (IOException e) {
					// Can't send message to the others
					logStructure.error("UDPDELEGATE Dispatcher leader can't send UDP message", e);
					e.printStackTrace();
				}
			}
			
			
		}

	}
	
	public class UDPDelegateWhenNotConnected implements UDPMulticastDelegate, launchable
	{

		private boolean Connected = false;
		private boolean StopElection = false;
		
		public void receivedData(UDPString message, DatagramPacket packet) 
		{
			// Comportement e adopter lorsque l'on desire se connecter au reseau
			if (message.equals(DATA.IAMTHEDISPATCHERLEADER))
			{
				// on a reeu un message du dispatcher leader ! soit on peut se connecter !
				this.Connected = true;
				LeaderIP = packet.getAddress().getHostAddress();
			}
			
			if (message.toString().startsWith(DATA.HELLOIAMADISPATCHER.toString()))
			{
				// on regarde si l'id est plus grand que le sien !
				String numid = message.toString().substring(DATA.HELLOIAMADISPATCHER.toString().length(), message.toString().length());
				int number = new Integer(numid).intValue();
				
				if (number > ID)
				{
					// soit, ici, un autre message d'election est reçu !
					// on doit dire d'arrêter l'élection ici !
					//logStructure.info("UDPDELEGATE stopping the election");
					this.StopElection = true;
				}
			}
		}

		public void launch() {
			
			// on s'arrange pour que l'élection puisse avoir lieu évidemment !
			
			this.Connected = false; 
			this.StopElection = false;
			
			// on set le delegate toujours en premier !
			manager.setDelegate(delegateWhenNotConnected);
			manager.delDelegate(delegateWhenLeader);
			manager.delDelegate(delegateWhenDispatcher);
			
			//System.out.println("je suis deconnecte !");
			logStructure.info("UDPDELEGATE I'm not connected, waiting for the dispatcher leader");
			
			
			// ensuite on essaie de se connecter au dispatcher leader
			
			// on va lancer une élection, 
			for (int i =0; i < DATA.RETREIVENUMBER && !(this.Connected || this.StopElection ); i+=1)
			{
				// On essaie de se connecter au dispatcher leader
				try {
					manager.sendData(new UDPString(DATA.HELLOIAMADISPATCHER.toString() + ID)); // ça c'est le message d'élection !
				} catch (IOException e) {
					// Shit, pas moyen d'envoyer des packets !
					logStructure.error("UDPDELEGATE Dispatcher can't send UDP message", e);
					e.printStackTrace();
				}
				// On fait une petite pause
				new Timer(500);
			}
			
			// si on nous demande d'arrêter l'election, il suffit d'attendre de se connecter !
			// évidemment, si on à rien demander au niveau de l'arrêt de l'élection, 
			if (this.StopElection)
			{
				while (!this.Connected)
				{
					new Timer(200);
				}
			}
			
			if (!this.Connected)
			{
				// malheureusement nous n'avons pas su joindre le dispatcher leader,
				// soit nous devenons le leader !
				((launchable)delegateWhenLeader).launch();
			}
			else
			{
				// on a reussi e se connecter au dispatcher leader, on devient donc un dispatcher normal
				((launchable)delegateWhenDispatcher).launch();
			}
		}
	}
	private boolean stopelection = false;
	
	private boolean dosomething = true;
	
	public class UDPDelegateWhenDispatcher implements UDPMulticastDelegate, launchable
	{
		private boolean electionthreadalreadylaunched = false;
		
		public class tryelection implements Runnable
		{
			
			public void run() {
				while (true)
				{
				try{
				
				
				while (!dosomething)
				{
					new Timer(100); // on fait une pause de 100 ms;
				}
				
				{
					dosomething = false;
					
					logStructure.info("UDPDELEGATE with ID " + ID + " sending new message...");
					
				stopelection = false;	
				// TODO Auto-generated method stub
				// ici le nombre de fois qu'on va le dire dépend de l'ID, donc, se sera toujours le plus grand qui l'emporte !
				for (int i=0; i < DATA.RETREIVENUMBER + ID && !stopelection ; i+=1)
				{
					
					logStructure.info("UDPDELEGATE width ID " + ID + " sending message !");
					manager.sendData(new UDPString(DATA.HELLOIAMADISPATCHER.toString() + ID));
					new Timer(500);
					
				}
				// si rien n'est reçu de la part du leader, alors on s'élit leader !
				if (!stopelection)
				{
					
					try{
					
					// soit on dit que c'est le nouveau leader, et il envoie cela en broadcast !
						if (!Iamtheleader) // évidemment, on ne doit pas être leader !
						{

							logStructure.info("DISPATCHER ELECTION I should become the leader ! " + ID);
							//							
							// on envoie cela en broadcast !
							try{
							//manager.sendData(DATA.IAMTHEDISPATCHERLEADER);
							}
							catch (Exception e)
							{
								logStructure.error("DISPATCHER ELECTION Can't send UDP Message !");
							}
							
							((launchable)delegateWhenLeader).launch();
						}
						else
						{
							logStructure.info("DISPATCHER ELECTION I am already the leader ! " + ID);
							// hé ben oui, faut le dire aux autres !
							manager.sendData(DATA.IAMTHEDISPATCHERLEADER); 
						}
					}
					catch (Exception e)
					{
						logStructure.error("DISPATCHER ELECTION Can't register the delegate !");
					}
					
					
					
				}
				
				}
				
				} 
				
				catch (Exception e)
				{
					logStructure.error("TRYING ELECTION Error while sending messages");
				}
				
				} // fin du while true !
				
			}}
		

		public void receivedData(UDPString message, DatagramPacket packet) 
		{
			// Comportement e adopter lorsque l'on est un simple dispatcher
			// là on regarde s'il n'y a pas un message d'élection qui serait d'ID inférieur !
			if (message.equals(DATA.IAMTHEDISPATCHERLEADER))
			{
				try{
					
					// si on recoit le message du dispatcher leader,
					// on arrête l'election
					
					stopelection = true;
				}
				catch (Exception e)
				{
					/*
					 * 
					 * 
					 * 
					 * 
					 */
				}
			}
			
			if (message.toString().startsWith(DATA.HELLOIAMADISPATCHER.toString()))
			{
				
				try
				{
				// on prend l'ID du message ;
				String idnum = message.toString().substring(DATA.HELLOIAMADISPATCHER.toString().length(), message.toString().length());
				//logStructure.info("UDPDELEGATE with ID " + ID + " Receiving election message, ID of the source : " + idnum);
				if (new Integer(idnum).intValue() < ID  && !Iamtheleader)
				{
					// Soit, ici, on peut relancer un message HELLOIAMADISPATCHER avec le bon ID;
					// message d'élection a envoyer !
					
					dosomething = true;
					if (!electionthreadalreadylaunched)
					{	// on peut lancer le thread pour qque chose !
						electionthreadalreadylaunched = true;
						Thread th = new Thread(new tryelection());
						th.start();
						
					}
					
				}
				else
				{
					if (new Integer(idnum).intValue() > ID)
					{
					// ici, l'id reçu est plus grand !
						stopelection = true;
					}
				}
				
				}
				catch (Exception e)
				{
					logStructure.error("UDPDELEGATE Error in the formating, of message, aborting !");
				}
			}
		}
		
		public void launch() {
			// on set le delegate toujours en premier !
			
			manager.setDelegate(delegateWhenDispatcher);
			manager.delDelegate(delegateWhenNotConnected);
			manager.delDelegate(delegateWhenLeader);
			
			//System.out.println("je suis un dispatcher ordinaire !");
			logStructure.info("UDPDELEGATE I'm an ordinary dispatcher connected to the leader");
			
			
			// je vais ouvrir une connexion vers le dispatcher leader ;
			
			try {
				// On desire se connecter au dispatcher leader afin d'obtenir son IOR
				InetAddress addr = InetAddress.getByName(LeaderIP);
				
				Socket s = new Socket(addr, DATA.PORTTCPLEADERFROMDISPATCHER);
				
				// le client envoie le premier son IOR
				OutputStream output = s.getOutputStream();
				output.write(IOR.getBytes());
				
				// le client attend ensuite l'IOR du dispatcher leader !
				InputStream input = s.getInputStream();
				int length = input.read(b);
				
				// Fermeture de la connexion !
				s.close();
				
				LeaderIOR = (new String(b) ).substring(0, length);
				dispatcherProxy = new DispatcherProxy(LeaderIOR);
				//System.out.println("IOR du Leader :"+LeaderIOR);
				
				
				
			} catch (UnknownHostException e) {
				// ne sais pas s'y connecter !
				logStructure.error("UDPDELEGATE Dispatcher can't send message", e);
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logStructure.error("UDPDELEGATE Dispatcher can't send message", e);
				//e.printStackTrace();
			}
		}
		
	}
	
	public class MaintainConnected implements Runnable
	{

		public void run() {
			// permet de continuer e faire vivre les workers !
			while (true)
			{
				new Timer(DATA.TIMEBEFORERECONNECTING / 8); // on fait une mini pause, mais pas trop longtemps !
			
				for (int i =0; i<workers.size() && Iamtheleader; i+=1)
				{
					// soit tous les workers
					if (workers.get(i) != null)
					{
						WorkerProxy wo = (WorkerProxy) workers.get(i);
						if (wo != null)
						{	
						try
						{
							//logStructure.info("Waking up worker number : " + i);
							// ici on fait les worker les uns e la suite des autres... 
							// il faut faire attention e ne faire trop de latence sur le reseau !
							wo.wakeUp();
						}
						catch (Exception e)
						{
							// oups, probleme avec le worker !
							//System.out.println("oups, probleme avec le worker : " + wo);
							logStructure.error("Connection lost with the worker : " + wo + " with the ID : " + i);
							
							// on l'enleve de la liste !
							workers.set(i, null);
						}
						}
					}
				}
				
				new Timer (DATA.TIMEBEFORERECONNECTING / 8); // on refait la mini pause...
				
			}
		}
		
	}
	
	
	public DispatcherImplemented getInstance()
	{
		// Retourne l'instance de l'objet Corba du dispatcher !
		return this.instance;
	}
	
	public DispatcherProxy getDispatcherLeader()
	{
		// retourne l'instance proxy du dispatcher leader !
		return this.dispatcherProxy;
	}
	
	// on pourrait simplement avoir un thread, vérifiant tout le temps si le dispatcher leader est présent !
	// sinon, il relance l'élection !
	
	public class leaderSentinelle implements Runnable
	{

		public void run() {
			// TODO Auto-generated method stub
			int essais=0;
			while (true)
			{
				try
				{
					
					new Timer(500); // soit c'est toutes les secondes !
					if (essais < 4)
					{
						essais += 1; // soit pendant les quelques premières secondes, on ne teste pas !
					}
					else
					{
						// évidemment, c'est uniquement s'il on est pas leader !
						
						if (!Iamtheleader) 
						{
							getDispatcherLeader().dearLeaderAreUStillThere();
							//logStructure.info("Ok, dispatcher leader responding !");
						}
					}
					
				}
				catch (Exception e)
				{
					logStructure.info("Connection lost with the leader, launching election !");
					((launchable) delegateWhenNotConnected).launch();
					
					new Timer(5000); // on peut facilement attendre 8 secondes ! 
					// de toute façon, même sans leader, les workers continuent leur travail !
					essais = 10 ; // on autorise plus les essais !
				}
				
			}
		}
		
	}
}
