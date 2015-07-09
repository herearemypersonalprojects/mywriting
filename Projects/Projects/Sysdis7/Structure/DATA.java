package Structure;

import java.io.EOFException;

import Connector.UDPString;

public class DATA {

	// L'IP utilis�e pour l'envoi de messages UDP multicast
	public final static String IPSESSIONMULTICAST = "239.0.0.1";
	
	// Le port utilis� pour l'envoi de messages UDP multicast
	public final static int PORTSESSIONMULTICAST = 15000;

	// Le port utilis� pour l'envoi de messages TCP pour les dispatcher avec le dispatcher leader
	public final static int PORTTCPLEADERFROMDISPATCHER = 15001;
	
	// Le port utilis� pour l'envoi de messages TCP pour les worker avec le dispatcher leader
	public final static int PORTTCPLEADERFROMWORKER = 15002;

	// Le port utilis� pour l'envoi de messages TCP pour les client avec le dispatcher leader
	public final static int PORTTCPLEADERFROMCLIENT = 15003;
	
	// Message pour signaler qu'un dispatcher veut se connecter
	public final static UDPString HELLOIAMADISPATCHER = new UDPString("HELLOIAMADISPATCHER");
	
	// Message pour signaler qu'un worker veut se connecter
	public final static UDPString HELLOIAMAWORKER = new UDPString("HELLOIAMAWORKER" );
	
	// Message pour signaler qu'un client veut se connecter
	public final static UDPString HELLOIAMACLIENT = new UDPString("HELLOIAMACLIENT");
	
	// Message envoy� par le dispatcher leader pour dire que c'est lui !
	public final static UDPString IAMTHEDISPATCHERLEADER = new UDPString("IAMTHEDISPATCHERLEADER");
	
	// Temps en ms qu'un composant doit attendre pour �tre s�r qu'il est tjs connect� au dispatcher leader !
	public final static long TIMEBEFORERECONNECTING = 20000; // ici 8 sec...
	
	// Temps en ms pour r�-emettre les t�ches ! 
	public final static long TIMEBEFOREREASKINGCOMPUTINGTASK = 4000; // ici 4 secondes !
	
	// Chemin d'acc�s au fichier de properties utilis�s par Log4g
	public final static String LOG4GPROPERTIESFILE = "src/Structure/PROP1.properties";
	
	// Nombre de ré-essai pour l'élection...
	public final static int RETREIVENUMBER = 3;
	
	// Epaisseur de découpe utilisée
	public final static int CHUNKHEIGHT = 24;
}
