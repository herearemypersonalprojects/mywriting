package Business.Courtier.Data;

import java.util.ArrayList;
import java.util.Iterator;



import Utils.Date;
import Utils.Euro;
import Utils.ID;
import Utils.Identifiant;
import Utils.MoneyCurrency;
import Utils.Quantite;

public class Ordre {

	private CompteTitres comptetit;
	private ID numordre;
	private Date datepymentfrais;
	private boolean estatoutprix;
	private boolean estunachat;
	private ID numordreenbourse;
	private String etat;
	private Quantite quantitedesiree;
	private Date datedeb;
	private Quantite quantiterealisee;
	private Date datefin;
	private MoneyCurrency montantlimite;
	private SocieteCotee societe;

	private ArrayList titresdelordre;
	private ArrayList Transactions = new ArrayList();
	private ArrayList LignesJournal = new ArrayList();
	public static boolean retainComputedTransactions = true;
	public static boolean retainComputedLignesJournal = true;

	
	
	private Transaction constructTransaction(ID numtransaction)
	{
		String Montant = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select Montant from COU_transaction where numtransaction='"+numtransaction.getID()+ "'", "montant");
		String DateDe = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select DateDe from COU_transaction where numtransaction='"+numtransaction.getID()+ "'", "DateDe");
		String QuantiteRealisee = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select QuantiteRealisee from COU_transaction where numtransaction='"+numtransaction.getID()+ "'", "QuantiteRealisee");
		String Numsociete = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select numsociete from COU_transaction where numtransaction='"+numtransaction.getID()+ "'", "numsociete");
		
		
		ID numsocietea = new Identifiant(Numsociete);
		
		Quantite querealisea = new Quantite(new Integer(QuantiteRealisee).intValue());
		Date datedea = new Date(DateDe);
		
		MoneyCurrency montanta = new Euro(new Float(Montant).floatValue());
		
		// soit on prend le num de la société, et l'on va chercher avec le rootData, la société à y lier !
		SocieteCotee lasociete = null;
		Iterator soc = this.getCompteTitres().getCompteLiquidites().getParticulier().getCourtier().getRootData().getSocietesCotees();
		boolean found = false;
		while (soc.hasNext() && !found)
		{
			SocieteCotee soci = (SocieteCotee)soc.next();
			String num = soci.getNumeroSociete();
			if (num != null && num.equals(numsocietea.getID()))
			{
				found = true;
				lasociete = soci;
			}
		}
		if (!found)
		{
			throw new RuntimeException("Error ; society not found => that's not normal !");
		}
		// on devra également binder tous les titres de l'ordre !
		Iterator titres = Business.Courtier.DB.getDataManager.ExecuteToDB("select NumTitre from COU_titre where numtransaction='" + numtransaction.getID() + "' and numsociete ='" + lasociete.getNumeroSociete() + "'", "NumTitre").iterator();
		ArrayList titresdelatransaction = new ArrayList(); 
		while (titres.hasNext())
		{
			ID numtitredan = new Identifiant(titres.next().toString());
			Iterator i1 = lasociete.getTitres();
			boolean found2 = false;
			while (i1.hasNext() && !found2)
			{
				Titre tit = (Titre) i1.next();
				if (tit.getNumeroTitre().equals(numtitredan.getID()))
				{
					found2 = true;
					titresdelatransaction.add(tit);
				}
			}
			if (!found2)
				throw new RuntimeException("Titre non trouve ! ce n'est pas normal !");
		}
		
		return new Transaction(this, numtransaction, datedea, montanta, querealisea, lasociete, titresdelatransaction);
		//return null;
		//return new Ordre(this, numordre, datepaymentfr, estattpri, estunacha, numordrebourse, Etat, quadesiree, querealise, datedeb, datefin, montantlimi, lasociete, titresdelordre );
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
		if (retainComputedLignesJournal ) this.LignesJournal = answer;
		return answer;
	}
	
	// permet de renvoyer la liste des transactions de l'ordre !
	private ArrayList getInnerTransactions()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.Transactions.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// là il faut le construire et l'ajouter dans la réponse !
				answer.add(this.constructTransaction((ID)ob));
			}
			if (ob instanceof Transaction)
			{
				// on l'ajoute directement !
				answer.add(ob);
			}
			
		}
		if (retainComputedTransactions  ) this.Transactions = answer;
		return answer;
	}
	
	
	public Ordre(CompteTitres comptetit, ID numordre, Date datepaymentfr, boolean estattpri, boolean estunacha, ID numordrebourse, String etat, Quantite quadesiree, Quantite querealise, Date datedeb, Date datefin, MoneyCurrency montantlimi, SocieteCotee lasociete, ArrayList titresdelordre) {
		
		this.comptetit = comptetit;
		this.numordre = numordre;
		this.datepymentfrais = datepaymentfr;
		this.estatoutprix = estattpri;
		this.estunachat = estunacha;
		this.numordreenbourse = numordrebourse;
		this.etat = etat;
		this.quantitedesiree = quadesiree;
		this.quantiterealisee = querealise;
		this.datedeb = datedeb;
		this.datefin = datefin;
		this.montantlimite = montantlimi;
		this.societe = lasociete;
		this.titresdelordre = titresdelordre; // c'est ici que les titres sont initialisés !
		// il faut ensuite associer la societe et les titres à l'ordre !
		this.societe.bindOrdre(this);
		Iterator it = this.titresdelordre.iterator();
		while (it.hasNext())
		{
			Titre tit = (Titre) it.next();
			tit.bindOrdre(this);
		}
		Iterator lignejournal = Business.Courtier.DB.getDataManager.ExecuteToDB("select numeroligne from COU_lignejournal where lienrapide='" +this.numordre.getID() + "'", "numeroligne").iterator();
		while (lignejournal.hasNext())
		{
			ID ligne = new Identifiant(lignejournal.next().toString());
			// on ajoute uniquement l'identifiant !
			this.LignesJournal .add(ligne);
		}
		// maintenant il faut obtenir toutes les transaction au sujet de l'ordre
		Iterator transactions = Business.Courtier.DB.getDataManager.ExecuteToDB("select numtransaction from COU_transaction where numordre='" +this.numordre.getID() + "'", "numtransaction").iterator();
		while (transactions.hasNext())
		{
			ID transaction = new Identifiant(transactions.next().toString());
			// on ajoute uniquement l'identifiant !
			this.Transactions.add(transaction);
		}
		// voilà, c'est tout !
		
	}
	
	public CompteTitres getCompteTitres()
	{
		return this.comptetit;
	}
	
	public String getNumeroOrdre()
	{
		return this.numordre.getID();
	}
	
	public String getDatePaymentFrais()
	{
		return this.datepymentfrais.getDate();
	}

	public boolean getEstAtoutPrix()
	{
		return this.estatoutprix;
	}
	public boolean getEstUnAchat()
	{
		return this.estunachat;
	}
	public String getNumeroOrdreEnBourse()
	{
		return this.numordreenbourse.getID();
	}
	public String getEtat()
	{
		return this.etat;
	}
	public String getQuantiteDesiree()
	{
		return this.quantitedesiree.getQuantite();
	}
	
	public String getQuantiteRealisee()
	{
		return this.quantiterealisee.getQuantite();
	}
	public String getDateDebut()
	{
		return this.datedeb.getDate();
	}
	public String getDateButoir()
	{
		return this.datefin.getDate();
	}
	public String getMontantLimite()
	{
		return this.montantlimite.getAmount();
	}
	public SocieteCotee getSociete()
	{
		return this.societe;
	}
	public Iterator getTitresAssociated()
	{
		return this.titresdelordre.iterator();
	}
	public Iterator getLignesJournal()
	{
		return this.getInnerLignesJournal().iterator();
	}
	
	public Iterator getTransactions()
	{
		return this.getInnerTransactions().iterator();
	}
	// on peut pouvoir modifier l'état de l'ordre,
	// la date de payment des frais,
	// le numéro de l'ordre en bourse !
	// la quantite réalisée
	
	public void setEtat(String etat)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_ordre set etat='" + etat + "' where numordre='" + this.getNumeroOrdre() + "'");
		this.etat = etat;
	}
	
	public void setDatePayementFrais(Date da)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_ordre set DatePaymentFrais=" + da.getDate() + " where numordre='" + this.getNumeroOrdre() + "'");
		this.datepymentfrais = da;
	}
	
	public void setNumeroOrdreEnBourse(ID numero)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_ordre set NumOrdreEnBourse='" + numero.getID() + "' where numordre='" + this.getNumeroOrdre() + "'");
		this.numordreenbourse = numero;
	}
	public void setQuantiteRealisee(Quantite qu)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_ordre set QuantiteRealisee=" + qu.getQuantite() + " where numordre='" + this.getNumeroOrdre()+ "'");
		this.quantiterealisee =qu;
	}
	// on doit pouvoir ajouter une transaction, une ligne journal, des titres !
	public void addNewLigneJournal(LigneJournal lig)
	{
		if (lig.getObjetLienRapide() != this)
			throw new RuntimeException("probleme ajout d'une ligne journal pour le particulier ! pas de correspondance !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_lignejournal (HeureEvenement, TypeOperation, SurTypeObjet, DateEvenement, DejaConsulteParParticulier, DejaConsulteParCourtier, Commentaire, NumeroLigne, LienRapide) values ('" + lig.getHeureEvenement()+ "','" + lig.getTypeOperation() + "','" + lig.getSurTypeObjet() + "','" + lig.getDateEvenement() + "','" + new String(""+lig.getDejaConsulteParParticulier()).toUpperCase()+ "','"+ new String (""+lig.getDejaConsulteParCourtier()).toUpperCase() + "','"+lig.getCommentaire() + "','" + lig.getNumeroDeLigne() + "','" + this.getNumeroOrdre() + "'" + ")");
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
	public void addTitre (Titre tit)
	{
		// on chercher d'abord s'il n'existe pas !
		boolean found = false;
		Iterator it = this.getTitresAssociated();
		while (it.hasNext() && !found)
		{
			Titre ti = (Titre) it.next();
			if (ti.getNumeroTitre().equals(tit.getNumeroTitre()))
				found = true;
		}
		if (!found)
		{
			// il faut setter le titre vers ce compte de titres
			tit.setOrdre(this);
			this.titresdelordre.add(tit);
		}
	}
	
	public void removeTitre(Titre tit)
	{
//		 on chercher d'abord s'il n'existe pas !
		boolean found = false;
		Iterator it = this.getTitresAssociated();
		while (it.hasNext() && !found)
		{
			Titre ti = (Titre) it.next();
			if (ti.getNumeroTitre().equals(tit.getNumeroTitre()))
				found = true;
		}
		if (found)
		{
			// il faut unsetter le titre vers ce compte de titres
			tit.unsetOrdre(this);
			this.titresdelordre.remove(tit);
		}
	}
	// maintenant on ajoute une transaction !
	public void addNewTransaction(Transaction tra)
	{
		if (tra.getOrdre() != this)
			throw new RuntimeException("probleme ajout d'une transaction, ordre non correspondant !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_transaction (DateDe, NumTransaction, Montant, QuantiteRealisee, NumSociete, NumOrdre) values (" + tra.getDate() + ",'" + tra.getNumeroTransaction() + "'," + tra.getMontant() + "," + tra.getQuantiteRealisee() + ",'" + tra.getSociete().getNumeroSociete() + "','" + this.getNumeroOrdre() + "')");
		
		if (Business.Courtier.DB.getDataManager.DBactive)
		{
			this.Transactions.add(new Identifiant(tra.getNumeroTransaction()));
			
		}
		else
		{
			this.Transactions.add(tra);
		}
	}
	
	
	
}
