package Structure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import Connector.UDPMulticast;
import Connector.UDPMulticastDelegate;
import Connector.UDPString;
import CorbaGeneric.Server;
import CorbaObject.DispatcherProxy;
import CorbaObject.WorkerImplemented;

public class Worker {

	// on peut consid�rer les workers comme des clients,
	// comme cela tout le monde peut utiliser le service !
	
	private UDPMulticast manager;
	private String IOR;
	
	private String LeaderIP;
	private String LeaderIOR;
	
	private byte[] b = new byte[1024*1024]; // soit un tampon de 1Mo => utilis� dans les �changes TCP
	
	private final UDPMulticastDelegate delegateWhenNotConnected = new UDPDelegateWhenNotConnected();
	private final UDPMulticastDelegate delegateWhenConnected = new UDPDelegateWhenConnected();
	
	private final Logger logStructure = Logger.getLogger(this.getClass());
	
	// Instance de l'objet Corba du worker;
	private WorkerImplemented instance;
	
	private DispatcherProxy dispatcherProxy = null;
	
	public void reset()
	{
		
		// on relance compl�tement le worker, phase de r�initialisation etc,...
		
		this.logStructure.info("STRUCTURE Worker restarted probably due to disconnection with the leader");
		// on met le proxy du dispatcher à null !
		this.dispatcherProxy = null;
		
		((launchable) this.delegateWhenNotConnected).launch();
	}
	
	public Worker()
	{
		// Il faudra cr�er l'objet CORBA encapsul� ici...
		// Je mets � jour l'IOR...
		this.instance = new WorkerImplemented(this);
		this.IOR = new Server().addCorbaObject(this.instance);
		this.logStructure.info("Servant CORBAObject Worker running");
		
		// puis l� on essaie de joindre le syst�me !
		// Pour joindre les dispatcher's, on utilise toujours 
		try 
		{
			this.manager = new UDPMulticast(DATA.IPSESSIONMULTICAST, DATA.PORTSESSIONMULTICAST);
			
			((launchable) this.delegateWhenNotConnected).launch();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.logStructure.error("UDPSESSIONMULTICAST failed", e);
			e.printStackTrace();
			
		}
		
		
	}
	
	public class UDPDelegateWhenConnected implements UDPMulticastDelegate, launchable
	{

		public void receivedData(UDPString message, DatagramPacket packet) {
			// Ce que je dois faire si je suis connect� et re�ois un packet multicast...
			
		}

		public void launch() 
		{
			manager.setDelegate(delegateWhenConnected);
			manager.delDelegate(delegateWhenNotConnected);
			
			//System.out.println("je suis un worker !");
			logStructure.info("UDPDELEGATE I'm a worker connected to the leader");
			
			
			try {
				// On d�sire se connecter au dispatcher leader afin d'obtenir son IOR
				InetAddress addr = InetAddress.getByName(LeaderIP);
				Socket s = new Socket(addr, DATA.PORTTCPLEADERFROMWORKER);
				
				// le client envoie le premier son IOR
				OutputStream output = s.getOutputStream();
				output.write(IOR.getBytes());
				
				// le client attend ensuite l'IOR du dispatcher leader !
				InputStream input = s.getInputStream();
				int length = input.read(b);
				
				// Fermeture de la connexion !
				s.close();
				
				LeaderIOR = (new String(b) ).substring(0, length);
				// cr�ation du proxy
				dispatcherProxy = new DispatcherProxy(LeaderIOR);
				//System.out.println("IOR du leader :"+LeaderIOR);
				
				
				
			} catch (UnknownHostException e) {
				// ne sais pas s'y connecter !
				logStructure.error("UDPSESSIONMULTICAST failed", e);
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logStructure.error("UDPSESSIONMULTICAST failed", e);
				e.printStackTrace();
				
			}
			// une fois que tout c'est bien pass�,
			// on essai de maintenir la connexion :
			instance.maintainConnected();
		}
		
	}
	
	public class UDPDelegateWhenNotConnected implements UDPMulticastDelegate, launchable
	{

private boolean Connected = false;
		
		public void receivedData(UDPString message, DatagramPacket packet) 
		{
			// Comportement � adopter lorsque l'on d�sire se connecter au r�seau
			if (message.equals(DATA.IAMTHEDISPATCHERLEADER))
			{
				// on a re�u un message du dispatcher leader ! soit on peut se connecter !
				this.Connected = true;
				LeaderIP = packet.getAddress().getHostAddress();
			}
		}

		public void launch() {
			// on set le delegate toujours en premier !
			this.Connected = false;
			
			manager.setDelegate(delegateWhenNotConnected);
			manager.delDelegate(delegateWhenConnected);
			
			//System.out.println("je suis deconnecte !");
			logStructure.info("UDPDELEGATE I'm not connected, waiting for the dispatcher leader");
			
			// ensuite on essaie de se connecter au dispatcher leader
			
			while(!this.Connected)
			{
				// On essaie de se connecter au dispatcher leader
				try {
					manager.sendData(DATA.HELLOIAMAWORKER);
				} catch (IOException e) {
					// Shit, pas moyen d'envoyer des packets !
					logStructure.error("UDPSESSIONMULTICAST failed", e);
					//e.printStackTrace();
					
				}
				// On fait une petite pause
				new Timer(500);
			}
			// Ok, on a atteint le dispatcher leader !
			((launchable)delegateWhenConnected).launch();
		}
		
	}
	
	public WorkerImplemented getInstance()
	{
		// Retourne l'instance Corba du worker
		return this.instance;
	}
	
	public DispatcherProxy getDispatcherLeader()
	{
		// retourne l'instance proxy du dispatcher leader !
		return this.dispatcherProxy;
	}
	
}
