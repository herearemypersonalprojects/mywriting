package Structure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.omg.CORBA.IntHolder;

import Connector.UDPMulticast;
import Connector.UDPMulticastDelegate;
import Connector.UDPString;
import CorbaGeneric.Server;
import CorbaObject.ClientImplemented;
import CorbaObject.DispatcherHelper;
import CorbaObject.DispatcherImplemented;
import CorbaObject.DispatcherProxy;

public class Client {

	// on peut considérer les workers comme des clients,
	// comme cela tout le monde peut utiliser le service !
	
	private UDPMulticast manager;
	private String IOR;
	
	private String LeaderIP;
	private String LeaderIOR;
	
	private byte[] b = new byte[1024*1024]; // soit un tampon de 1Mo => utilisé dans les échanges TCP
	
	private final UDPMulticastDelegate delegateWhenNotConnected = new UDPDelegateWhenNotConnected();
	private final UDPMulticastDelegate delegateWhenConnected = new UDPDelegateWhenConnected();
	
	private final Logger logStructure = Logger.getLogger(this.getClass());
	
	// L'instance de l'objet Corba
	private ClientImplemented instance;
	//  L'instance du dispatcher leader est nulle, car à priori on a pas encore connaissance du dispatcher leader !
	private DispatcherProxy dispatcherProxy = null;
	
	public Client()
	{
		// Il faudra créer l'objet CORBA encapsulé ici...
		// -- todo --
		// Je mets à jour l'IOR...
		// Attention, ici ce n'est pas le bon objet !!!
		this.instance = new ClientImplemented(this); 
		this.IOR = new Server().addCorbaObject(instance);
		this.logStructure.info("Servant CORBAObject Client running");
		
		// puis là on essaie de joindre le système !
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
			// Ce que je dois faire si je suis connecté et reçois un packet multicast...
			
		}

		public void launch() 
		{
			manager.setDelegate(delegateWhenConnected);
			manager.delDelegate(delegateWhenNotConnected);
			
			//System.out.println("je suis un client !");
			logStructure.info("UDPDELEGATE I'm a client connected to the leader");
			
			
			try {
				// On désire se connecter au dispatcher leader afin d'obtenir son IOR
				InetAddress addr = InetAddress.getByName(LeaderIP);
				Socket s = new Socket(addr, DATA.PORTTCPLEADERFROMCLIENT);
				
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
		}
		
	}
	
	public class UDPDelegateWhenNotConnected implements UDPMulticastDelegate, launchable
	{

private boolean Connected = false;
		
		public void receivedData(UDPString message, DatagramPacket packet) 
		{
			// Comportement à adopter lorsque l'on désire se connecter au réseau
			if (message.equals(DATA.IAMTHEDISPATCHERLEADER))
			{
				// on a reçu un message du dispatcher leader ! soit on peut se connecter !
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
					manager.sendData(DATA.HELLOIAMACLIENT);
				} catch (IOException e) {
					// Shit, pas moyen d'envoyer des packets !
					logStructure.error("UDPSESSIONMULTICAST failed", e);
					e.printStackTrace();
					
				}
				// On fait une petite pause
				new Timer(500);
			}
			// Ok, on a atteint le dispatcher leader !
			((launchable)delegateWhenConnected).launch();
		}
		
	}

	
	public ClientImplemented getInstance()
	{
		// retourne l'instance du client corba
		return this.instance;
	}
	
	public DispatcherProxy getDispatcherLeader()
	{
		// retourne l'instance proxy du dispatcher leader !
		return this.dispatcherProxy;
	}
	
	public void close() {
		// pour arrêter l'objet, il suffit d'arrêter d'écouter la connexion
		// permet d'être simple !
		try
		{
		manager.delDelegate(delegateWhenConnected);
		manager.delDelegate(delegateWhenNotConnected);
		Server.orb.destroy();
		// System.out.println("Client closed");
		this.logStructure.info("Client correctly closed");
		}
		catch (Exception e)
		{
		this.logStructure.error("STRUCTURED Client failed when closing app's", e);
		}
	}
}
