package Business.Courtier.Data;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.ID;
import Utils.Identifiant;
import Utils.MoneyCurrency;

public class CompteLiquidites {

	private MoneyCurrency soldeDisponible;
	private MoneyCurrency soldeCourant;
	private ID numCompteLiquidites;
	private Particulier possedePar;
	
	private ArrayList ComptesTitres = new ArrayList();
	
	private ArrayList LignesJournal = new ArrayList();
	
	// Même chose pour les lignes journal
	public static boolean retainComputedLignesJournal = true;
	// Même chose pour les comptes de titres
	public static boolean retainComputedComptesTitres = true;
	
	// Construit un compte de titres à partir de son identifiant !
	private CompteTitres constructCompteTitres(ID numcomptetitres)
	{
		
		return new CompteTitres(numcomptetitres, this);
		
	}
	
	
	//	 Retourne un ArrayList avec les lignesjournal du courtier directement construite !
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

//	 Retourne un ArrayList avec les comptesdetitres directement construite !
	private ArrayList getInnerComptesTitres()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.ComptesTitres.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// là il faut le construire et l'ajouter dans la réponse !
				answer.add(constructCompteTitres((ID) ob));
			}
			if (ob instanceof CompteTitres)
			{
				// on l'ajoute directement !
				answer.add(ob);
			}
			
		}
		if (retainComputedComptesTitres) this.ComptesTitres = answer;
		return answer;
	}
	
	
	public CompteLiquidites(ID numCompteLiquidites, MoneyCurrency soldeDisponible, MoneyCurrency soldeCourant, Particulier possedePar)
	{
		// Création d'un objet représentant une instance d'un compte de liquidités
		this.numCompteLiquidites = numCompteLiquidites;
		this.possedePar = possedePar;
		this.soldeCourant = soldeCourant;
		this.soldeDisponible = soldeDisponible;
		// On obtient de la DB tous les numéro des comptes de titres issus de ce compte de liquidités ;
		
		// soit cela permettra d'essayer de mettre à jour les données que l'on a déjà sauvegardées...
		Iterator comptesTitres = Business.Courtier.DB.getDataManager.ExecuteToDB("select numcomptetitres from COU_comptetitres where numcompteliquidites = '" + this.numCompteLiquidites.getID() + "'",	"numcomptetitres").iterator();
		while (comptesTitres.hasNext())
		{
			this.ComptesTitres.add(new Identifiant(comptesTitres.next().toString()));
		}
		
		// il faut aussi obtenir toutes les lignes du journal correspondant !
		Iterator lignejournal = Business.Courtier.DB.getDataManager.ExecuteToDB("select numeroligne from COU_lignejournal where lienrapide='" + this.numCompteLiquidites.getID() + "'", "numeroligne").iterator();
		while (lignejournal.hasNext())
		{
			ID ligne = new Identifiant(lignejournal.next().toString());
			// on ajoute uniquement l'identifiant !
			this.LignesJournal.add(ligne);
		}
		
	}
	
	public Iterator getLignesJournal()
	{
		// retourne les lignes journal au sujet de cet objet !
		return this.getInnerLignesJournal().iterator();
	}
	
	public Iterator getCompteTitres()
	{
		// retourne un itérateur sur l'ensemble des comptes de titres liés au compte de liquidités :
		return this.getInnerComptesTitres().iterator();
	}
	
	public String getSoldeDisponible()
	{
		// retourne le solde disponible du compte...
		return this.soldeDisponible.getAmount();
	}
	
	public String getSoldeCourant()
	{
		// retourne le solde courant du compte...
		return this.soldeCourant.getAmount();
	}
	
	public String getNumCompteLiquidites()
	{
		// retourne l'identifiant du compte de liquidites !
		return this.numCompteLiquidites.getID();
	}
	
	public Particulier getParticulier()
	{
		// retourne le particulier parent de l'objet !
		return this.possedePar;
	}
	
	public void setSoldeDisponible(MoneyCurrency argent)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_CompteLiquidites set soldedisponible=" + argent.getAmount() + " where numcompteliquidites='" + this.getNumCompteLiquidites() + "'");
		this.soldeDisponible = argent;
	}
	
	public void setSoldeCourant(MoneyCurrency argent)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_CompteLiquidites set soldecourant=" + argent.getAmount() + " where numcompteliquidites='" + this.getNumCompteLiquidites() + "'");
		this.soldeCourant = argent;
	}
	
	public void addNewLigneJournal(LigneJournal lig)
	{
		if (lig.getObjetLienRapide() != this)
			throw new RuntimeException("probleme ajout d'une ligne journal pour le compte de liquidites ! pas de correspondance !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_lignejournal (HeureEvenement, TypeOperation, SurTypeObjet, DateEvenement, DejaConsulteParParticulier, DejaConsulteParCourtier, Commentaire, NumeroLigne, LienRapide) values ('" + lig.getHeureEvenement()+ "','" + lig.getTypeOperation() + "','" + lig.getSurTypeObjet() + "','" + lig.getDateEvenement() + "','" + new String(""+lig.getDejaConsulteParParticulier()).toUpperCase()+ "','"+ new String (""+lig.getDejaConsulteParCourtier()).toUpperCase() + "','"+lig.getCommentaire() + "','" + lig.getNumeroDeLigne() + "','" + this.getNumCompteLiquidites() + "'" + ")");
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
	
	// il faut pouvoir ajouter un compte de titres !
	public void addNewCompteTitres(CompteTitres co)
	{
		if (co.getCompteLiquidites() != this)
			throw new RuntimeException ("probleme ajout d'un nouveau compte de titres, compte de liquidites non correspondant !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_CompteTitres (NumCompteTitres, NumCompteLiquidites) values ('" + co.getNumCompteTitres() + "','" + this.getNumCompteLiquidites() + "')");
		if (Business.Courtier.DB.getDataManager.DBactive)
			this.ComptesTitres.add(new Identifiant(co.getNumCompteTitres()));
		else
			this.ComptesTitres.add(co);
	}
}
