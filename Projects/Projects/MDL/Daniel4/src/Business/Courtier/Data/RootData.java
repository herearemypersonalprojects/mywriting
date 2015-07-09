package Business.Courtier.Data;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.Date;
import Utils.Euro;
import Utils.ID;
import Utils.Identifiant;
import Utils.MoneyCurrency;
import Utils.Time;

public class RootData {

	// La RootData classe correspond à un point de naissance de l'ensemble des données du système courtier
	// lors de sa construction il est important de fournir les données primaires en premier lieu
	// c'est à dire : les sociétés, leurs titres, éventuellement les cours des sociétés
	// ensuite seulement intervient la gestoin des courtier, particulier etc... jusque, finalement, aux transaction !
	
	public ArrayList SocietesCotees = new ArrayList();
	
	public ArrayList Courtiers = new ArrayList();
	
	// on ne peut accéder qu'à une instance unique du rootdata !
	private static RootData instance=null;
	
	// Afin d'optimiser le calcul, on gaspille plus de mémoire ; on enregistre chaque objet nouvellement créé
	public static boolean retainComputedSocietes = true;
	// même chose pour les courtiers !
	public static boolean retainComputedCourtiers = true;
	
	// Permet de reconstruire un courtier à partir de son ID;
	private Courtier constructCourtier(ID login)
	{
		// il faut : MotDePasseBourse, LoginBourse, Bloque, MotDePasse
		String MotDePasseBourse = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select MotDePasseBourse from COU_Courtier where login='"+login.getID()+ "'", "MotDePasseBourse");
		String LoginBourse = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select LoginBourse from COU_Courtier where login='"+login.getID()+ "'", "LoginBourse");
		String Bloque = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select Bloque from COU_Courtier where login='"+login.getID()+ "'", "Bloque");
		String MotDePasse = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select MotDePasse from COU_Courtier where login='"+login.getID()+ "'", "MotDePasse");
		boolean loque = new Boolean(Bloque).booleanValue();
		ID oginBourse = new Identifiant(LoginBourse);
		
		return (new Courtier(login, MotDePasse, oginBourse, MotDePasseBourse, loque, this));
	
	}
	
	// Permet de reconstruire une société à partir de son ID;
	private SocieteCotee constructSocieteCotee(ID numso)
	{
		// il faut : Bloquee, NomSociete, 
		String Bloquee = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select Bloquee from COU_SocieteCotee where Numsociete='"+numso.getID()+ "'", "Bloquee");
		String NomSociete = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select NomSociete from COU_SocieteCotee where Numsociete='"+numso.getID()+ "'", "NomSociete");
		
		boolean loquee = new Boolean(Bloquee).booleanValue();
		
		return (new SocieteCotee(numso, NomSociete, loquee, this));
	
	}
	
	//	 Retourne un ArrayList avec courtiers directement construite !
	private ArrayList getInnerCourtier()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.Courtiers.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// on doit le construire et l'ajouter !
				answer.add(constructCourtier((ID)ob));
			}
			if (ob instanceof Courtier)
			{
				// on l'ajoute directement !
				answer.add(ob);
			}
		}
		if (retainComputedCourtiers) this.Courtiers = answer; // cela évitera de recalculer...
		return answer;
	}
	
	// Retourne un ArrayList avec societescotees directement construite !
	private ArrayList getInnerSocieteCotee()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.SocietesCotees.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// on doit le construire et l'ajouter !
				answer.add(constructSocieteCotee((ID)ob));
			}
			if (ob instanceof SocieteCotee)
			{
				// on l'ajoute directement !
				answer.add(ob);
			}
		}
		if (retainComputedSocietes) this.SocietesCotees = answer; // cela évitera de recalculer...
		return answer;
	}
	
	private RootData()
	{
		System.out.println("Starting data initalisation");
		// il faut avant tout, obtenir les informations sur les societes ! => c'est très important
		// l'ordre ici est primordial !
		// c'est le même principe que les autres classes data du courtier !
		// remarque : il n'y a pas de contraintes sur la SFW, c'est normal, c'est la racine !
		Iterator societescotees = Business.Courtier.DB.getDataManager.ExecuteToDB("select NumSociete from COU_societecotee", "NumSociete").iterator();
		while (societescotees.hasNext())
		{
			ID societecotee = new Identifiant(societescotees.next().toString());
			// nous pouvons directement l'ajouter dans l'ArrayListe;
			SocietesCotees.add(societecotee);
			
		}
		this.getInnerSocieteCotee(); // phase d'initialisation ! cela permet de construire tous les titres et les sociétés !
		Iterator courtiers = Business.Courtier.DB.getDataManager.ExecuteToDB("select Login from COU_Courtier", "Login").iterator();
		while (courtiers.hasNext())
		{
			ID courtier = new Identifiant(courtiers.next().toString());
			// nous pouvons directement l'ajouter dans l'ArrayListe;
			Courtiers.add(courtier);
			
		}
		// voilà, c'est tout !
		System.out.println("Ended data initialisation");
	}
	
	
	
	public Iterator getCourtiers()
	{
		// renvoie la liste des courtiers !
		return this.getInnerCourtier().iterator();
	}
	
	public Iterator getSocietesCotees()
	{
		// renvoie la liste des sociétés cotées !
		return this.getInnerSocieteCotee().iterator();
	}
	
	public Courtier getCourtier(ID identifiantLoginDuCourtier)
	{
		Iterator dataToFind = this.getCourtiers();
		boolean found = false;
		Courtier answer = null;
		while (dataToFind.hasNext() && !found)
		{
			Courtier cou = (Courtier) dataToFind.next();
			if (cou.getLogin().equals(identifiantLoginDuCourtier.getID()))
			{
				found = true;
				answer = cou;
			}
		}
		if (!found)
		{
			throw new RuntimeException("Courtier avec l'identifiant : " + identifiantLoginDuCourtier.getID() + " non trouve !");
			
		}
		return answer;
	}
	public SocieteCotee getSociete(ID identifiantNumeroDeLaSociete)
	{
		Iterator dataToFind = this.getSocietesCotees();
		boolean found = false;
		SocieteCotee answer = null;
		while (dataToFind.hasNext() && !found)
		{
			SocieteCotee cou = (SocieteCotee) dataToFind.next();
			if (cou.getNumeroSociete().equals(identifiantNumeroDeLaSociete.getID()))
			{
				found = true;
				answer = cou;
			}
		}
		if (!found)
		{
			throw new RuntimeException("Societe avec l'identifiant : " + identifiantNumeroDeLaSociete.getID() + " non trouvee !");
			
		}
		return answer;
	}
	// ici les constructeurs considérés sont pour les nouveaux courtier, les nouvelles societes !
	
	public void addNewCourtier(Courtier cou)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_courtier (MotDePasseBourse, LoginBourse, Bloque, MotDePasse, login) values ('" +cou.getBourseMotDePasse()+"','"+cou.getBourseLogin() +"','" + cou.getBloque() + "','"+ cou.getMotDePasse() + "','"+ cou.getLogin()+"')");
		if (Business.Courtier.DB.getDataManager.DBactive)
			this.Courtiers.add(new Identifiant(cou.getLogin()));
		else
			this.Courtiers.add(cou);
	}
	
	public void addNewSociete(SocieteCotee soc)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_societecotee (Bloquee, NumSociete) values ('" + soc.getBloquee() + "','" + soc.getNumeroSociete() + "')");
		
		if (soc.getNomSociete() != null)
		{
			// on met le nom de la societe !
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_societecotee set nomsociete='" + soc.getNomSociete() + "' where numsociete='" + soc.getNumeroSociete() + "'");
		}
		if (Business.Courtier.DB.getDataManager.DBactive)
			this.SocietesCotees.add(new Identifiant(soc.getNumeroSociete()));
		else
			this.SocietesCotees.add(soc);
	}
	
	
	// mouais ! ça, ça vient en fin de classe !
	public static RootData getInstance()
	{
		// renvoie l'instance unique du rootData !
		if (instance == null)
			instance = new RootData();
		
		return instance;
	}
	
	// pour l'instant l'heure et la date ne sont pas encore encapsulés dans des objets CORBA...
	
	public static Time getTime()
	{
		return new Time(Utils.Utilities.getCalendarTime());
	}
	
	public static Date getDate()
	{
		return new Date(Utils.Utilities.getCalendarDate());
	}


	
}
