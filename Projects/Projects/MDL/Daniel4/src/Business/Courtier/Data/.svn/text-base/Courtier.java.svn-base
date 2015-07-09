package Business.Courtier.Data;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.Date;
import Utils.Euro;
import Utils.ID;
import Utils.Identifiant;
import Utils.MoneyCurrency;
import Utils.NullMoneyCurrency;
import Utils.Quantite;
import Utils.Time;

public class Courtier {

	private ID login;
	private String pass;
	private ID loginBourse;
	private String passBourse;
	private boolean bloque; // retient si le courtier est bloqué ou non !
	private RootData rootData;
	
	public ArrayList Particuliers = new ArrayList();
	public ArrayList Politiques = new ArrayList();
	public ArrayList Liquidations = new ArrayList();
	public ArrayList LignesJournal = new ArrayList();
	
	
	// Afin d'optimiser le calcul, on gaspille plus de mémoire ; on enregistre chaque objet nouvellement créé
	public static boolean retainComputedParticuliers = true;
	// Même chose pour les politiques
	public static boolean retainComputedPolitiques = true;
	// Même chose pour les lignes journal
	public static boolean retainComputedLignesJournal = true;
	
	// Permet de construire un particulier à partir de son ID !
	private Particulier constructParticulier(ID parti)
	{
		// il faut : nomentreprise, numerotva, estunepersonnemorale, bloque, motdepasse, prenom, nom
		String nomentreprise = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select nomentreprise from COU_particulier where login='" + parti.getID()+"'", "nomentreprise" );
		String numerotva = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select numerotva from COU_particulier where login='" + parti.getID()+"'", "numerotva" );
		String estunepersonnemorale = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select estunepersonnemorale from COU_particulier where login='" + parti.getID()+"'", "estunepersonnemorale" );
		String bloque = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select bloque from COU_particulier where login='" + parti.getID()+"'", "bloque" );
		String motdepasse = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select motdepasse from COU_particulier where login='" + parti.getID()+"'", "motdepasse" );
		String prenom = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select prenom from COU_particulier where login='" + parti.getID()+"'", "prenom" );
		String nom = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select nom from COU_particulier where login='" + parti.getID()+"'", "nom" );

		// La comparaison s'effectue tjs vis à vis de true ! c'est logique !
		boolean stunepersonnemorale = estunepersonnemorale.equals("TRUE");
		boolean loque = bloque.equals("TRUE");
		
		
		return (new Particulier(parti, nomentreprise, numerotva, stunepersonnemorale, loque, motdepasse, prenom, nom, this));
	}
	
	// Permet de construire un ligne journal à partir de son ID;
	
	
	// Permet de reconstruire une politique à partir de son ID;
	private Politique constructPolitique(ID datede)
	{
		// il faut : reglefacturation, seuillimiteliquidites, fraisemissionordre
		String reglefacturation = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select reglefacturation from COU_politique where datede='"+datede.getID()+ "'", "reglefacturation");
		String seuillimiteliquidites = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select seuillimiteliquidites from COU_politique where datede='"+datede.getID()+ "'", "seuillimiteliquidites");
		String fraisemissionordre = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select fraisemissionordre from COU_politique where datede='"+datede.getID()+ "'", "fraisemissionordre");
	
		// le seuil et le frais sont normalement des MoneyCurrency :
		MoneyCurrency euillimiteliquidites = new Euro(new Float(seuillimiteliquidites).floatValue());
		MoneyCurrency raisemissionordre = new Euro(new Float(fraisemissionordre).floatValue());
		
		return (new Politique(datede, euillimiteliquidites, raisemissionordre, reglefacturation, this));
	
	}
	
	// Retourne un ArrayList avec les politiques du courtier directement construite !
	private ArrayList getInnerPolitiques()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.Politiques.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// on doit le construire et l'ajouter !
				answer.add(constructPolitique((ID)ob));
			}
			if (ob instanceof Politique)
			{
				// on l'ajoute directement !
				answer.add(ob);
			}
		}
		if (retainComputedPolitiques) this.Politiques = answer; // cela évitera de recalculer...
		return answer;
	}
	
	// Retourne un ArrayList avec les lignesjournal du courtier directement construite !
	private ArrayList getInnerLignesJournal()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.LignesJournal.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// là il faut le construire et l'ajouter dans la réponse !
				answer.add(Journal.constructLigneJournal((ID) ob, this));
			}
			if (ob instanceof LigneJournal)
			{
				// on l'ajoute directement !
				answer.add(ob);
			}
			
		}
		if (retainComputedLignesJournal) this.LignesJournal = answer;
		return answer;
	}
	
	// Retourne un ArrayList avec les particuliers du courtier directement construite !
	private ArrayList getInnerParticulier()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.Particuliers.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// On doit le construire et l'ajouter !
				answer.add(constructParticulier((ID) ob));
			}
			if (ob instanceof Particulier)
			{
				// On l'ajoute directement !
				answer.add(ob);
			}
		}
		if (retainComputedParticuliers) this.Particuliers = answer; // cela évitera de recalculer les particuliers !
		return answer;
	}
	
	public Courtier(ID login, String pass, ID loginBourse, String passBourse, boolean bloque, RootData root)
	{
		this.bloque = bloque;
		this.login = login;
		this.pass = pass;
		this.loginBourse = loginBourse;
		this.passBourse = passBourse;
		this.rootData = root; // c'est la racine du système !
		
		// On va chercher tous les particuliers associés :
		Iterator parts = Business.Courtier.DB.getDataManager.ExecuteToDB("select login from COU_particulier where s_A_login = '" + this.login.getID() + "'",	"login").iterator();
		while (parts.hasNext())
		{
			ID parti = new Identifiant(parts.next().toString()); // soit l'identifiant d'un particulier ! nous ne sommes pas réellement obligés de construire cet objet maintenant !
			// nous pouvons maintenant l'ajouter à l'ArrayListe...
			this.Particuliers.add(parti);
		}
		// On va chercher toutes les politiques associées :
		Iterator politics = Business.Courtier.DB.getDataManager.ExecuteToDB("select datede from COU_politique where login='" + this.login.getID() + "'", "datede").iterator();
		while (politics.hasNext())
		{
			ID datede = new Identifiant(politics.next().toString());
			// nous pouvons directement l'ajouter dans l'ArrayListe;
			this.Politiques.add(datede);
		}
		
		// Il faut également obtenir toutes les informations concernant les liquidations
		// ici on les conserves par défaut !
		Iterator alldateliquidations = Business.Courtier.DB.getDataManager.ExecuteToDB("select datede from COU_Liquidation where login='" + this.login.getID() + "'", "datede").iterator();
		while (alldateliquidations.hasNext())
		{
			ID datede = new Identifiant(alldateliquidations.next().toString());// la première partie de l'identifiant !
			Iterator allautrecourtier = Business.Courtier.DB.getDataManager.ExecuteToDB("select autrecourtier from COU_liquidation where login='" + this.login.getID() + "' and datede='" + datede.getID() +"'", "autrecourtier" ).iterator();
			while (allautrecourtier.hasNext())
			{
				ID autrecourtier = new Identifiant(allautrecourtier.next().toString());
				// Ici, il est plus simple de forcer la construction des objets de liquidation... (car identifiant à deux composants)
				
				// il faut : le montant de la liquidation, savoir s'il est effectué ou pas !
				String dejaeffectue = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select esteffectue from COU_liquidation where datede='" + datede.getID()+"' and autrecourtier='" + autrecourtier.getID() +"'", "esteffectue" );
				String montant = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select montant from COU_liquidation where datede='" + datede.getID()+"' and autrecourtier='" + autrecourtier.getID() +"'", "montant" );
				
				boolean ejaeffectue = dejaeffectue.equals("TRUE");
				MoneyCurrency ontant = new Euro(new Float(montant).floatValue());
				
				this.Liquidations.add(new Liquidation(datede, autrecourtier, ejaeffectue, ontant, this));
			}
		}
		// Il faut finalement obtenir toutes les informations du journal propre à cet objet !!!
		Iterator lignejournal = Business.Courtier.DB.getDataManager.ExecuteToDB("select numeroligne from COU_lignejournal where lienrapide='" + this.login.getID() + "'", "numeroligne").iterator();
		while (lignejournal.hasNext())
		{
			ID ligne = new Identifiant(lignejournal.next().toString());
			// on ajoute uniquement l'identifiant !
			this.LignesJournal.add(ligne);
		}
		
	}
	
	public String getLogin()
	{
		// retourne le login du courtier
		return this.login.getID();
	}
	
	public String getMotDePasse()
	{
		// retourne le mot de passe du courtier
		return this.pass;
	}
	
	public String getBourseLogin()
	{
		// retourne le login utilisé en bourse par le courtier !
		return this.loginBourse.getID();
	}
	
	public String getBourseMotDePasse()
	{
		// retourne le mot de passe utilisé en bourse par le courtier !
		return this.passBourse;
	}
	
	public boolean getBloque()
	{
		// retourne si le courtier est bloqué ou non !
		return this.bloque;
	}
	
	public Iterator getParticuliers()
	{
		// retourne la liste des particuliers du courtier !
		return this.getInnerParticulier().iterator();
	}
	
	public Iterator getPolitiques()
	{
		// retourne l'ensemble des politiques du courtier !
		return this.getInnerPolitiques().iterator();
	}
	
	public Iterator getLiquidations()
	{
		// retourne l'ensemble des liquidations du courtier !
		return this.Liquidations.iterator();
	}
	
	public RootData getRootData()
	{
		// renvoie la racine des données du système courtier !
		return this.rootData;
	}
	
	public Iterator getLignesJournal()
	{
		// retourne les lignes du journal au sujet du courtier :
		return this.getInnerLignesJournal().iterator();
	}
	// La seule méthode "set" considérée est "bloque"
	public void addNewParticulier(Particulier part)
	{
		if (part.getCourtier() != this)
			throw new RuntimeException("Courtier non correct dans le particulier !");
		
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_Particulier (EstUnePersonneMorale,bloque, motdepasse,login, s_a_login) values ('" + (new String(part.getEstUnePersonneMorale()+"").toUpperCase()) + "','" + (new String(""+part.getBloque()).toUpperCase()) + "','" + part.getMotDePasse() +  "','"+ part.getLogin() + "','" + this.getLogin() + "')");
		
		if (part.getNom() != null)
		{
			// on met le nom
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_particulier set nom='" + part.getNom() + "' where login='" + part.getLogin() + "'");
		}
		if (part.getPrenom() != null)
		{
			// on met le prénom !
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_particulier set prenom='" + part.getPrenom() + "' where login='" + part.getLogin() + "'");
			
		}
		if (part.getNumeroTVA() != null)
		{
			// on met la tva
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_particulier set numeroTVA='" + part.getNumeroTVA() + "' where login='" + part.getLogin() + "'");
			
		}
		if (part.getNomEntreprise() != null)
		{
			// on met le nom de l'entreprise !
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_particulier set nomentreprise='" + part.getNomEntreprise() + "' where login='" + part.getLogin() + "'");
			
		}
		
		
		
		if (Business.Courtier.DB.getDataManager.DBactive) // si la db est active, l'id suffit !
			this.Particuliers.add(new Identifiant(part.getLogin()));
		else
			this.Particuliers.add(part); // si pas de db, l'objet est nécessaire !
	}
	
	public void addNewPolitique(Politique pol)
	{
		if (pol.getCourtier() != this)
			throw new RuntimeException("Courtier non correct dans la politique !");
		
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_politique (RegleFacturation, SeuilLimiteLiquidites, FraisEmissionOrdre, DateDe, Login) values ('" + pol.getRegleFacturation() + "'," + pol.getSeuilLimiteLiquidites() + "," + pol.getFraisEmissionOrdre() + ",'" + pol.getDateDe() + "','" + pol.getCourtier().getLogin()+"')");
		if (Business.Courtier.DB.getDataManager.DBactive)
			this.Politiques.add(new Identifiant(pol.getDateDe()));
		else
			this.Politiques.add(pol);
	}
	
	public void addNewLiquidation(Liquidation liqu)
	{
		if (liqu.getCourtier() != this)
			throw new RuntimeException("probleme ajout liquidation, courtier non correspondant !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_liquidation (EstEffectue, DateDe, Montant, AutreCourtier, Login) values ('" + new String(liqu.getEstDejaEffectue()+"").toUpperCase() + "'," + liqu.getDateDe() + "," + liqu.getMontant() + ",'" + liqu.getAutreCourtier() + "','" + this.getLogin()+  "')");
		this.Liquidations.add(liqu);
	}
	
	public void setBloque(boolean value)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_Courtier set bloque='" + new String(value + "").toUpperCase() + "' where login='" + this.getLogin() + "'");
		this.bloque = value;
	}
	
	public void addNewLigneJournal(LigneJournal lig)
	{
		if (lig.getObjetLienRapide() != this)
			throw new RuntimeException("probleme ajout d'une ligne journal pour le courtier ! pas de correspondance !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_lignejournal (HeureEvenement, TypeOperation, SurTypeObjet, DateEvenement, DejaConsulteParParticulier, DejaConsulteParCourtier, Commentaire, NumeroLigne, LienRapide) values ('" + lig.getHeureEvenement()+ "','" + lig.getTypeOperation() + "','" + lig.getSurTypeObjet() + "','" + lig.getDateEvenement() + "','" + new String(""+lig.getDejaConsulteParParticulier()).toUpperCase()+ "','"+ new String (""+lig.getDejaConsulteParCourtier()).toUpperCase() + "','"+lig.getCommentaire() + "','" + lig.getNumeroDeLigne() + "','" + this.getLogin() + "'" + ")");
		if (lig.getMontant() != null)
		{
			// on met à jour le montant !
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_lignejournal set montant=" + lig.getMontant() + " where numeroligne='" + lig.getNumeroDeLigne() + "'");
		}
		if (lig.getQuantite() != null)
		{
			// on met la quantite !
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_lignejournal set quantite=" + lig.getQuantite() + " where numeroligne='" + lig.getNumeroDeLigne() + "'");
		}
		if (Business.Courtier.DB.getDataManager.DBactive)
			this.LignesJournal.add(new Identifiant(lig.getNumeroDeLigne()));
		else
			this.LignesJournal.add(lig);
	}
	
	public Particulier getParticulier(ID identifiantLoginDuParticulier)
	{
		Iterator dataToFind = this.getParticuliers();
		boolean found = false;
		Particulier answer = null;
		while (dataToFind.hasNext() && !found)
		{
			Particulier cou = (Particulier) dataToFind.next();
			if (cou.getLogin().equals(identifiantLoginDuParticulier.getID()))
			{
				found = true;
				answer = cou;
			}
		}
		if (!found)
		{
			throw new RuntimeException("Particulier avec l'identifiant : " + identifiantLoginDuParticulier.getID() + " non trouve !");
			
		}
		return answer;
	}
}
