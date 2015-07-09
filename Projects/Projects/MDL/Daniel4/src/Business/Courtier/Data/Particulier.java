package Business.Courtier.Data;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.Euro;
import Utils.ID;
import Utils.Identifiant;
import Utils.MoneyCurrency;

public class Particulier {

	private ID parti;
	private String nomentreprise;
	private String numerotva;
	private boolean stunepersonnemorale;
	private boolean loque;
	private String motdepasse;
	private String prenom;
	private String nom;
	private Courtier courtier;
	
	// la liste des comptes de liquidités attachés au particulier !
	public ArrayList ComptesLiquidites = new ArrayList();
	public ArrayList LignesJournal = new ArrayList();
	
	// On sauve les comptes de liquidites calculés ?
	public static boolean retainComputedComptesLiquidites = true;
	
	// Même chose pour les lignes journal
	public static boolean retainComputedLignesJournal = true;
	
	
	// Permet de reconstruire un compte de liquidites à partir de son ID;
	private CompteLiquidites constructCompteLiquidites(ID numcompteliquidites)
	{
		// il faut : reglefacturation, seuillimiteliquidites, fraisemissionordre
		String soldedisponible = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select soldedisponible from COU_compteliquidites where numcompteliquidites='"+numcompteliquidites.getID()+ "'", "soldedisponible");
		String soldecourant = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select soldecourant from COU_compteliquidites where numcompteliquidites='"+numcompteliquidites.getID()+ "'", "soldecourant");
		
		MoneyCurrency oldedisponible = new Euro(new Float(soldedisponible).floatValue());
		MoneyCurrency oldecourant = new Euro(new Float(soldecourant).floatValue());
		
		return (new CompteLiquidites(numcompteliquidites, oldedisponible, oldecourant, this));
	
	}
	
	// Retourne un ArrayList avec les comptes de liquidites du particulier directement construites !
	private ArrayList getInnerComptesLiquidites()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.ComptesLiquidites.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// on doit le construire et l'ajouter !
				answer.add(constructCompteLiquidites((ID)ob));
			}
			if (ob instanceof CompteLiquidites)
			{
				// on l'ajoute directement !
				answer.add(ob);
			}
		}
		if (retainComputedComptesLiquidites) this.ComptesLiquidites = answer; // cela évitera de recalculer...
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

	public Particulier(ID parti, String nomentreprise, String numerotva, boolean stunepersonnemorale, boolean loque, String motdepasse, String prenom, String nom, Courtier courtier) {
		this.parti = parti;
		this.nomentreprise = nomentreprise;
		this.numerotva = numerotva;
		this.stunepersonnemorale = stunepersonnemorale;
		this.loque = loque;
		this.motdepasse = motdepasse;
		this.prenom = prenom;
		this.nom = nom;
		this.courtier = courtier;
		
		// Il faut également aller chercher tous les comptes de liquidites du particulier !
		Iterator comptesliquidites = Business.Courtier.DB.getDataManager.ExecuteToDB("select numcompteliquidites from COU_compteliquidites where login='" + this.parti.getID() + "'", "numcompteliquidites").iterator();
		while (comptesliquidites.hasNext())
		{
			ID numcompteliquidites = new Identifiant(comptesliquidites.next().toString());
			// nous pouvons directement l'ajouter dans l'ArrayListe;
			this.ComptesLiquidites.add(numcompteliquidites);
		}
		
		// Puis toutes les lignes du journal le concernant !
		Iterator lignejournal = Business.Courtier.DB.getDataManager.ExecuteToDB("select numeroligne from COU_lignejournal where lienrapide='" + this.parti.getID() + "'", "numeroligne").iterator();
		while (lignejournal.hasNext())
		{
			ID ligne = new Identifiant(lignejournal.next().toString());
			// on ajoute uniquement l'identifiant !
			this.LignesJournal.add(ligne);
		}
	}

	public Courtier getCourtier()
	{
		// retourne le courtier auquel le particulier est attaché
		return this.courtier;
	}
	
	public String getLogin()
	{
		// retourne le login du particulier
		return this.parti.getID();
	}
	
	public boolean getBloque()
	{
		// retourne si le pariculier est bloqué ou non !
		return this.loque;
	}
	
	public boolean getEstUnePersonneMorale()
	{
		// retourne si le particulier est une personne morale;
		return this.stunepersonnemorale;
	}
	
	public String getNom()
	{
		// retourne le nom du particulier
		return this.nom;
	}
	
	public String getPrenom()
	{
		// retourne le prénom du particulier
		return this.prenom;
	}
	
	public String getMotDePasse()
	{
		// retourne le mot de passe du particulier;
		return this.motdepasse;
	}
	
	public String getNumeroTVA()
	{
		// retourne le numéro de TVA du particulier
		return this.numerotva;
	}
	
	public String getNomEntreprise()
	{
		// retourne le nom de l'entreprise
		return this.nomentreprise;
	}
	
	public Iterator getComptesLiquidites()
	{
		// retourne les comptes de liquidites du particulier une fois calculés !
		return this.getInnerComptesLiquidites().iterator();
	}
	
	public Iterator getLignesJournal()
	{
		// retourne les lignes du journal concernant ce particulier !
		return this.getInnerLignesJournal().iterator();
	}
	
	// pour le set, on considère seulement le bloque
	public void setBloque(boolean value)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_particulier set bloque='" + new String(value + "").toUpperCase() + "' where login='" + this.getLogin() + "'");
		this.loque = value;
	}
	
	public void addNewLigneJournal(LigneJournal lig)
	{
		if (lig.getObjetLienRapide() != this)
			throw new RuntimeException("probleme ajout d'une ligne journal pour le particulier ! pas de correspondance !");
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
	
	public void addNewCompteLiquidites(CompteLiquidites co)
	{
		if (co.getParticulier() != this)
			throw new RuntimeException ("probleme ajout d'un compte de liquidites, particulier non correspondant !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_CompteLiquidites (SoldeDisponible, NumCompteLiquidites, SoldeCourant, Login) values (" + co.getSoldeDisponible() + ",'" +  co.getNumCompteLiquidites()+"'," + co.getSoldeCourant() + ",'" + this.getLogin()+"'"+ ")");
		if (Business.Courtier.DB.getDataManager.DBactive)
			this.ComptesLiquidites.add(new Identifiant(co.getNumCompteLiquidites()));
		else
			this.ComptesLiquidites.add(co);
	}
	
	// Renvoie un compte de liquidites bien précis !
	public CompteLiquidites getCompteLiquidites(ID identifiantNumeroCompteLiquidites)
	{
		Iterator dataToFind = this.getComptesLiquidites();
		boolean found = false;
		CompteLiquidites answer = null;
		while (dataToFind.hasNext() && !found)
		{
			CompteLiquidites cou = (CompteLiquidites) dataToFind.next();
			if (cou.getNumCompteLiquidites().equals(identifiantNumeroCompteLiquidites.getID()))
			{
				found = true;
				answer = cou;
			}
		}
		if (!found)
		{
			throw new RuntimeException("Compte de liquidités avec le numéro de compte : " + identifiantNumeroCompteLiquidites.getID() + " non trouve !");
			
		}
		return answer;
	}
	
}
