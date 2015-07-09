package Business.Courtier.Data;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.Date;
import Utils.Euro;
import Utils.ID;
import Utils.Identifiant;
import Utils.MoneyCurrency;
import Utils.Quantite;

public class CompteTitres {

	private ID id;
	private CompteLiquidites numCompteLiquidites;
	
	// représentation des lignes du journal concernant ce compte de titres !
	private ArrayList LignesJournal= new ArrayList();
	// la liste des ordres propres à ce compte de titres !
	private ArrayList Ordres = new ArrayList();
	// la liste des titres propres à ce compte de titres !
	private ArrayList Titres = new ArrayList();
	
	
	// Même chose pour les lignes journal
	public static boolean retainComputedLignesJournal = true;
	// même chose pour les ordres
	public static boolean retainComputedOrdres = true;
	
	//	 Permet de reconstruire un ordre à partir de son ID;
	private Ordre constructOrdre(ID numordre)
	{
		// il faut : DatePaymentFrais, EstAToutPrix, EstUnAchat, NumOrdreEnBourse, Etat, QuantiteDesiree, QuantiteRealisee, DateDebut, DateButoir, MontantLimite
		// il va falloir faire attention au numéro de la société !
		String DatePaymentFrais = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select DatePaymentFrais from COU_ordre where numordre='"+numordre.getID()+ "'", "DatePaymentFrais");
		String EstAToutPrix = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select EstAToutPrix from COU_ordre where numordre='"+numordre.getID()+ "'", "EstAToutPrix");
		String EstUnAchat = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select EstUnAchat from COU_ordre where numordre='"+numordre.getID()+ "'", "EstUnAchat");
		String NumOrdreEnBourse = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select NumOrdreEnBourse from COU_ordre where numordre='"+numordre.getID()+ "'", "NumOrdreEnBourse");
		String Etat = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select Etat from COU_ordre where numordre='"+numordre.getID()+ "'", "Etat");
		String QuantiteDesiree = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select QuantiteDesiree from COU_ordre where numordre='"+numordre.getID()+ "'", "QuantiteDesiree");
		String QuantiteRealisee = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select QuantiteRealisee from COU_ordre where numordre='"+numordre.getID()+ "'", "QuantiteRealisee");
		String DateDebut = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select DateDebut from COU_ordre where numordre='"+numordre.getID()+ "'", "DateDebut");
		String DateButoir = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select DateButoir from COU_ordre where numordre='"+numordre.getID()+ "'", "DateButoir");
		String MontantLimite = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select MontantLimite from COU_ordre where numordre='"+numordre.getID()+ "'", "MontantLimite");
		
		Date datepaymentfr = new Date(DatePaymentFrais);
		boolean estattpri = EstAToutPrix.equals("TRUE");
		boolean estunacha = EstUnAchat.equals("TRUE");
		ID numordrebourse = new Identifiant(NumOrdreEnBourse);
		Quantite quadesiree = new Quantite(new Integer(QuantiteDesiree).intValue());
		Quantite querealise = new Quantite(new Integer(QuantiteRealisee).intValue());
		Date datedeb = new Date(DateDebut);
		Date datefin = new Date(DateButoir);
		
		MoneyCurrency montantlimi = new Euro(new Float(MontantLimite).floatValue());
		
		// soit on prend le num de la société, et l'on va chercher avec le rootData, la société à y lier !
		String NumSociete = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select NumSociete from COU_ordre where numordre='"+numordre.getID()+ "' group by numsociete", "NumSociete");
		SocieteCotee lasociete = null;
		Iterator soc = this.getCompteLiquidites().getParticulier().getCourtier().getRootData().getSocietesCotees();
		boolean found = false;
		while (soc.hasNext() && !found)
		{
			SocieteCotee soci = (SocieteCotee)soc.next();
			String num = soci.getNumeroSociete();
			if (num != null && num.equals(NumSociete))
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
		Iterator titres = Business.Courtier.DB.getDataManager.ExecuteToDB("select NumTitre from COU_titre where numordre='" + numordre.getID() + "' and numsociete='" + lasociete.getNumeroSociete()+ "'", "NumTitre").iterator();
		ArrayList titresdelordre = new ArrayList(); 
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
					titresdelordre.add(tit);
				}
			}
			if (!found2)
				throw new RuntimeException("Titre non trouve ! ce n'est pas normal !");
		}
		
		
		return new Ordre(this, numordre, datepaymentfr, estattpri, estunacha, numordrebourse, Etat, quadesiree, querealise, datedeb, datefin, montantlimi, lasociete, titresdelordre );
	}
	
	// Retourne un ArrayList avec les lignesjournal du compte directement construite !
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
	
	// Retourne un ArrayList avec les ordres du compte de titres construit !s
	private ArrayList getInnerOrdres()
	{
		ArrayList answer = new ArrayList();
		Iterator it = this.Ordres.iterator();
		while (it.hasNext())
		{
			Object ob = it.next();
			if (ob instanceof ID)
			{
				// là il faut construire et l'ajouter dans la réponse !
				answer.add(this.constructOrdre((ID) ob));
			}
			if (ob instanceof Ordre)
			{
				// on l'ajoute directemetn !
				answer.add(ob);
			}
		}
		if (retainComputedOrdres) this.Ordres = answer;
		return answer;
	}

	
	public CompteTitres(ID numcomptetitres, CompteLiquidites liquidites) {
		
		this.id = numcomptetitres;
		this.numCompteLiquidites = liquidites;
		// On cherche toutes les lignes du journal référencant cet objet !
		Iterator lignejournal = Business.Courtier.DB.getDataManager.ExecuteToDB("select numeroligne from COU_lignejournal where lienrapide='" + this.id.getID() + "'", "numeroligne").iterator();
		while (lignejournal.hasNext())
		{
			ID ligne = new Identifiant(lignejournal.next().toString());
			// on ajoute uniquement l'identifiant !
			this.LignesJournal.add(ligne);
		}
		// il faudra également prendre : l'ensemble des ordres
		Iterator ordres = Business.Courtier.DB.getDataManager.ExecuteToDB("select numordre from COU_Ordre where numcomptetitres = '" + this.id.getID() + "'", "numordre").iterator();
		while(ordres.hasNext())
		{
			ID or = new Identifiant(ordres.next().toString());
			this.Ordres.add(or);
		}
		// il faut ensuite s'occuper des titres du compte de titres !
		// pour se faire on regarde l'ensemble des sociétés cotées des titres associés...
		Iterator societees = Business.Courtier.DB.getDataManager.ExecuteToDB("select Numsociete from COU_titre where numcomptetitres ='" + this.id.getID() + "' group by numsociete","numsociete").iterator();
		while (societees.hasNext())
		{
			ID soc = new Identifiant(societees.next().toString()); 
			boolean found = false;
			Iterator soctofind = this.getCompteLiquidites().getParticulier().getCourtier().getRootData().getSocietesCotees();
			SocieteCotee societe = null;
			while (soctofind.hasNext() && !found)
			{
				societe = (SocieteCotee) soctofind.next();
				if (societe.getNumeroSociete().equals(soc.getID()))
				{
					// on a trouvé la societe !
					found = true;
				}
			}
			if (!found)
			{
				throw new RuntimeException("Societee non trouvee, ce n'est pas normal !");
			}
			// ici on a trouvé la société et elle se trouve dans la variable societe...
			// il faut maintenant reprendre tous les titres de cette sociétés à associés au compte...
			Iterator titres = Business.Courtier.DB.getDataManager.ExecuteToDB("select numtitre from COU_titre where numcomptetitres ='" + this.id.getID() + "' and numsociete = '" + societe.getNumeroSociete() + "'","numtitre").iterator();
			while (titres.hasNext())
			{
				ID tit = new Identifiant(titres.next().toString());
				// soit on a le titre issu de la base de données, il faut maintenant le retrouver dans les datas !
				boolean found2 = false;
				Titre titre = null;
				Iterator tittofind = societe.getTitres();
				while (tittofind.hasNext() && !found2)
				{
					titre = (Titre) tittofind.next();
					// on regarde si c'est le bon !
					if (titre.getNumeroTitre().equals(tit.getID()))
					{
						// on l'a trouvé !
						found2 = true;
					}
				}
				if (!found2)
				{
					throw new RuntimeException("Titre non trouve, ce n'est pas normal !");
				}
				// ici le titre a été trouvé, on l'ajoute à la liste des titres du compte de titres...
				this.Titres.add(titre);
				// on bind le titre
				titre.bindCompteTitres(this);
			}
			
		}
	}
	
	public String getNumCompteTitres()
	{
		// retourne l'identifiant du compte de titres
		return this.id.getID();
	}
	
	public CompteLiquidites getCompteLiquidites()
	{
		// retourne le compte de liquidités associé !
		return this.numCompteLiquidites;
	}
	
	public Iterator getLignesJournal()
	{
		// retourne les lignes journal au sujet de cet objet !
		return this.getInnerLignesJournal().iterator();
	}
	
	public Iterator getOrdres()
	{
		// retourne les ordrs
		return this.getInnerOrdres().iterator();
	}
	
	public Iterator getTitres()
	{
		// retourne les titres de ce compte de titres ;
		return this.Titres.iterator();
	}
	
	public void addNewLigneJournal(LigneJournal lig)
	{
		if (lig.getObjetLienRapide() != this)
			throw new RuntimeException("probleme ajout d'une ligne journal pour le compte de titres !! pas de correspondance !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_lignejournal (HeureEvenement, TypeOperation, SurTypeObjet, DateEvenement, DejaConsulteParParticulier, DejaConsulteParCourtier, Commentaire, NumeroLigne, LienRapide) values ('" + lig.getHeureEvenement()+ "','" + lig.getTypeOperation() + "','" + lig.getSurTypeObjet() + "','" + lig.getDateEvenement() + "','" + new String(""+lig.getDejaConsulteParParticulier()).toUpperCase()+ "','"+ new String (""+lig.getDejaConsulteParCourtier()).toUpperCase() + "','"+lig.getCommentaire() + "','" + lig.getNumeroDeLigne() + "','" + this.getNumCompteTitres() + "'" + ")");
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
	
	// il faut pouvoir également ajouter, supprimer un titre à ce compte !
	public void addTitre (Titre tit)
	{
		// on chercher d'abord s'il n'existe pas !
		boolean found = false;
		Iterator it = this.getTitres();
		while (it.hasNext() && !found)
		{
			Titre ti = (Titre) it.next();
			if (ti.getNumeroTitre().equals(tit.getNumeroTitre()))
				found = true;
		}
		if (!found)
		{
			// il faut setter le titre vers ce compte de titres
			tit.setCompteTitres(this);
			this.Titres.add(tit);
		}
	}
	
	public void removeTitre(Titre tit)
	{
//		 on chercher d'abord s'il n'existe pas !
		boolean found = false;
		Iterator it = this.getTitres();
		while (it.hasNext() && !found)
		{
			Titre ti = (Titre) it.next();
			if (ti.getNumeroTitre().equals(tit.getNumeroTitre()))
				found = true;
		}
		if (found)
		{
			// il faut unsetter le titre vers ce compte de titres
			tit.unsetCompteTitres(this);
			this.Titres.remove(tit);
		}
	}
	
	public void addNewOrdre(Ordre or)
	{
		if (or.getCompteTitres() != this)
			throw  new RuntimeException("probleme pour l'ajout d'un ordre, compte de titres non correpsondant !");
		Business.Courtier.DB.getDataManager.ExecuteToDB("insert into COU_ordre (EstAToutPrix, EstUnAchat, Etat, NumOrdre, QuantiteDesiree, QuantiteRealisee, DateDebut, DateButoir, MontantLimite, NumSociete, NumCompteTitres) values ('" + new String(""+or.getEstAtoutPrix()).toUpperCase()+ "','" +new String (""+or.getEstUnAchat()).toUpperCase() + "','" + or.getEtat() + "','" + or.getNumeroOrdre() + "'," + or.getQuantiteDesiree() + ", " + or.getQuantiteRealisee() + ", " + or.getDateDebut() + ", " + or.getDateButoir() + ", " + or.getMontantLimite()+ ",'" + or.getSociete().getNumeroSociete() + "', '" + this.getNumCompteTitres() + "')" );
		
		if (or.getDatePaymentFrais() != null)
		{
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_ordre set DatePaymentFrais=" + or.getDatePaymentFrais() + " where NumOrdre='" + or.getNumeroOrdre() + "'");
			
		}
		if (or.getNumeroOrdreEnBourse() != null)
			Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_ordre set NumOrdreEnBourse='" + or.getNumeroOrdreEnBourse() + "' where numordre='" + or.getNumeroOrdre() + "'");
		
		if (Business.Courtier.DB.getDataManager.DBactive)
			this.Ordres.add(new Identifiant(or.getNumeroOrdre()));
		else
			this.Ordres.add(or);
			
	}
	
}
