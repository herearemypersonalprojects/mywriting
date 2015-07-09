package Business.Courtier.Data;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.Date;
import Utils.Euro;
import Utils.ID;
import Utils.Identifiant;
import Utils.Time;

public class SocieteCotee {

	// la racine des données du système courtier !
	private RootData rootData;
	private ID numso;
	private String nomSociete;
	private boolean bloquee;
	
	private ArrayList Titres = new ArrayList();
	private ArrayList Cours = new ArrayList();
	private ArrayList LignesJournal = new ArrayList();
	private ArrayList ordres = new ArrayList();
	
	// variable permettant de savoir s'il on conserve les lignes journal calculées ou non !
	public static boolean retainComputedLignesJournal = true;
	
//	 Retourne un ArrayList avec les lignesjournal de la société directement construite !
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
		if (retainComputedLignesJournal ) this.LignesJournal = answer;
		return answer;
	}
	
	public SocieteCotee(ID numso, String nomSociete, boolean loquee, RootData data) {
		// les valeurs de l'objet :
		this.rootData = data;
		this.numso = numso;
		this.nomSociete = nomSociete;
		this.bloquee = loquee;
		
		// il faut obtenir les informations concernant : les titres, les cours, les lignes du journal,
		
		// soit d'abord les titres :
		Iterator titres = Business.Courtier.DB.getDataManager.ExecuteToDB("select numtitre from COU_Titre where numsociete = '" + this.numso.getID() + "'",	"numtitre").iterator();
		while (titres.hasNext())
		{
			ID titre = new Identifiant(titres.next().toString());
			
			this.Titres.add(new Titre(titre, this));
			
		}
		// on cherche à obtenir les différents cours ;
		Iterator coursdate = Business.Courtier.DB.getDataManager.ExecuteToDB("select datede from COU_courstitre where numsociete = '" + this.numso.getID() + "'",	"datede").iterator();
		while (coursdate.hasNext())
		{
			
			ID coursdatede = new Identifiant(coursdate.next().toString());
			// il faut également prendre l'heure de post.
			Iterator coursheure = Business.Courtier.DB.getDataManager.ExecuteToDB("select heure from COU_courstitre where numsociete = '" + this.numso.getID() + "' and datede = '" + coursdatede.getID() + "'",	"heure").iterator();
			while (coursheure.hasNext())
			{
				ID coursheurede = new Identifiant(coursheure.next().toString());
				String valeur = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select valeur from COU_courstitre where numsociete = '" + this.numso.getID() + "' and date de = '" + coursdatede.getID() + "' and heure = '" + coursheurede + "'", "valeur");
				
				float val = new Float(valeur).floatValue();
				
				this.Cours.add(new CoursTitre(new Date(coursdatede.getID()), new Time(coursheurede.getID()), this, new Euro(val)));
			}
		}
		// maintenant on cherche les lignes journal
		Iterator lignejournal = Business.Courtier.DB.getDataManager.ExecuteToDB("select numeroligne from COU_lignejournal where lienrapide='" + this.numso.getID() + "'", "numeroligne").iterator();
		while (lignejournal.hasNext())
		{
			ID ligne = new Identifiant(lignejournal.next().toString());
			// on ajoute uniquement l'identifiant !
			this.LignesJournal .add(ligne);
		}
		
	}
	public Iterator getCours()
	{
		// retourne la liste des cours de la société...
		return this.Cours.iterator();
	}
	
	public Iterator getTitres()
	{
		// retourne la liste des titres de la société...
		return this.Titres.iterator();
	}
	
	public String getNumeroSociete()
	{
		// retourne le numéro de la société considérée
		return this.numso.getID();
	}
	
	public String getNomSociete()
	{
		// retourne le nom de la société
		return this.nomSociete;
	}
	
	public void bindOrdre(Ordre ord)
	{
		// permet d'associer un ordre avec un entreprise !
		this.ordres .add(ord);
	}
	
	public boolean getBloquee()
	{
		// retourne si la societe est bloquée
		return this.bloquee;
	}
	
	public RootData getRootData()
	{
		// retourne le rootData du système courtier !
		return this.rootData;
	}

	public Iterator getLignesJournal()
	{
		// retourne les lignes du journal au sujet du courtier :
		return this.getInnerLignesJournal().iterator();
	}
	
	public Iterator getOrdres()
	{
		// retourne l'ensemble des ordres liés à cette sociétés ;
		return this.ordres.iterator();
	}
	
	public void addNewLigneJournal(LigneJournal lig)
	{
		if (lig.getObjetLienRapide() != this)
			throw new RuntimeException("probleme ajout d'une ligne journal pour le courtier ! pas de correspondance !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_lignejournal (HeureEvenement, TypeOperation, SurTypeObjet, DateEvenement, DejaConsulteParParticulier, DejaConsulteParCourtier, Commentaire, NumeroLigne, LienRapide) values ('" + lig.getHeureEvenement()+ "','" + lig.getTypeOperation() + "','" + lig.getSurTypeObjet() + "','" + lig.getDateEvenement() + "','" + new String(""+lig.getDejaConsulteParParticulier()).toUpperCase()+ "','"+ new String (""+lig.getDejaConsulteParCourtier()).toUpperCase() + "','"+lig.getCommentaire() + "','" + lig.getNumeroDeLigne() + "','" + this.getNumeroSociete() + "'" + ")");
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
	
	public void setBloque(boolean value)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_societecotee set bloquee='" + new String(value + "").toUpperCase() + "' where numsociete='" + this.getNumeroSociete() + "'");
		this.bloquee = value;
	}
	
	// on doit pouvoir ajouter un cours, ainsi qu'un titre !
	public void addNewCours(CoursTitre cou)
	{
		if (cou.getSociete() != this)
		{
			throw new RuntimeException("erreur ajout d'un cours, societe non correspondante !");
		}
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_CoursTitre (NumSociete, Heure, Valeur, DateDe) values ('" + this.getNumeroSociete() + "'," + cou.getHeure() + "," + cou.getValeur() + "," +cou.getDate() + ")");
		this.Cours.add(cou);
	}
	
	public void addNewTitre(Titre tit)
	{
		if (tit.getSociete() != this)
			throw new RuntimeException("erreur ajout d'un titre, societe non correspondante !");
		
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_titre (NumSociete, NumTitre) values ('" + this.getNumeroSociete() + "','" + tit.getNumeroTitre() + "')");
		// on suppose ici que le titre est forcément, non lié !
		this.Titres.add(tit);
	}
	
}
