package Business.Courtier.Mains;

import java.sql.SQLException;
import java.util.Iterator;

import Business.Courtier.Data.CompteLiquidites;
import Business.Courtier.Data.CompteTitres;
import Business.Courtier.Data.Courtier;
import Business.Courtier.Data.LigneJournal;
import Business.Courtier.Data.Ordre;
import Business.Courtier.Data.Particulier;
import Business.Courtier.Data.Politique;
import Business.Courtier.Data.RootData;
import Business.Courtier.Data.SocieteCotee;
import Business.Courtier.Data.Titre;
import Business.Courtier.Data.Transaction;
import Business.Courtier.Managers.CourtierManager;
import Business.Courtier.Managers.ParticulierManager;
import Business.Courtier.Managers.PoliticsManager;
import Business.Courtier.Managers.UsersManager;
import CorbaObject.CourtierForParticulierImplemented;
import Utils.Date;
import Utils.Euro;
import Utils.Identifiant;
import Utils.NullMoneyCurrency;
import Utils.Quantite;
import Utils.Time;
import Utils.SQL.SQLDB;
import Utils.SQL.SQLUser;

public class startCourtierSystem {

	public static void main(String[] args) {
		// Start the courtier system 
		
		// setting data to access the DB
		Business.Courtier.DB.getDataManager.db = new SQLDB("localhost","1521", "mydev");
		Business.Courtier.DB.getDataManager.usr = new SQLUser("system", "nema19", Business.Courtier.DB.getDataManager.db);
		
		// setting up prefix for courtier DB
		Business.Courtier.DB.getDataManager.prefix = "COU_";
		
		// activating or not the DB
		Business.Courtier.DB.getDataManager.DBactive = false;
		
		if (true) // permet de spécifier si l'on doit détruire la bd
			Business.Courtier.DB.getDataManager.destroyDB();
		
		if (true) // permet de spécifier si l'on doit constuire la bd
			Business.Courtier.DB.getDataManager.constructDB();
		
		// upward remarks should be managed by an external properties file
		
		//while (true)
		
		{
		/*	
		Iterator soc = RootData.getInstance().getSocietesCotees();
		while (soc.hasNext())
		{
			SocieteCotee soci = (SocieteCotee) soc.next();
			System.out.println(soci.getNumeroSociete());
			Iterator ti = soci.getTitres();
			while (ti.hasNext())
			{
				Titre tit = (Titre) ti.next();
				System.out.println(tit.getNumeroTitre()+ " " + tit.getCompteTitres());
			}
		}
		*/	
			
			
		
		if(CourtierManager.createNewCourtier(new Identifiant("Daniel"), "Clarisse", "salut", "grr")) System.out.println("ok"); else System.out.println("ko");
		//if (CourtierManager.createNewCourtier(new Identifiant("Daniel"), "Clarisse", "salut", "grrr")) System.out.println("ok"); else System.out.println("ko");
		
		if(PoliticsManager.createNewPolitique(new Identifiant("Daniel"), PoliticsManager.REGLEFACTURATION_APRESPREMIERERECEPTION, new Euro(-1000), new Euro(5))) System.out.println("ok"); else System.out.println("ko");
		
		
		Courtier dan = RootData.getInstance().getCourtier(new Identifiant("Daniel"));
		
		if (ParticulierManager.addNewParticulier(new Identifiant("Clarisse"), false, null, null, "je t aime", "Clarisse", "Roebroek", new Identifiant("Daniel"))) System.out.println("ok"); else System.out.println("ko");
		
		UsersManager.bloqueCourtier(new Identifiant("Daniel"), "j ai envie de faire ça !");
		
		UsersManager.bloqueParticulier(new Identifiant("Clarisse"), "grrrrr", new Identifiant("Daniel"));
		
		UsersManager.debloqueCourtier(new Identifiant("Daniel"), "cool");
		
		
		Iterator cour = RootData.getInstance().getCourtiers();
		
		while (cour.hasNext())
		{
			Courtier cou = (Courtier) cour.next();
			
		{
		Iterator test = cou.getParticuliers();
		while (test.hasNext())
		{
			Particulier part = (Particulier) test.next();
			System.out.println(part.getLogin());
			
			Iterator test4 = part.getLignesJournal();
			while (test4.hasNext())
			{
				LigneJournal lig = (LigneJournal)test4.next();
				System.out.println(lig.getCommentaire() + " " + lig.getObjetLienRapide());
			}
			Iterator test3 = part.getComptesLiquidites();
			while (test3.hasNext())
			{
				CompteLiquidites comp = (CompteLiquidites) test3.next();
				System.out.println(comp.getNumCompteLiquidites() + " " + comp.getSoldeCourant() + " " + comp.getSoldeDisponible());
				Iterator test5 = comp.getLignesJournal();
				while (test5.hasNext())
				{
					LigneJournal ligne = (LigneJournal) (test5.next());
					System.out.println(ligne.getCommentaire() + " " + ligne.getMontant() + " " + ligne.getQuantite());
				
				}
				Iterator test6 = comp.getCompteTitres();
				while (test6.hasNext())
				{
					CompteTitres tit = (CompteTitres) test6.next();
					System.out.println(tit.getNumCompteTitres());
					Iterator t = tit.getTitres();
					while (t.hasNext())
					{
						Titre ta = (Titre) t.next();
						System.out.println("daniel - titre : " + ta.getNumeroTitre() + " " + ta.getSociete().getNumeroSociete() + " " + ta.getCompteTitres());
					}
					
					Iterator test7 = tit.getLignesJournal();
					while (test7.hasNext())
					{
						LigneJournal ligne = (LigneJournal) test7.next();
						System.out.println(ligne.getCommentaire() + " " + ligne.getQuantite() + " " + ligne.getMontant());
					}
					Iterator da1 = tit.getOrdres();
					while (da1.hasNext())
					{
						Ordre or = (Ordre) da1.next();
						System.out.println("*** Ordre : " + or.getNumeroOrdre() + " " + or.getSociete().getNumeroSociete());
						Iterator da2 = or.getLignesJournal();
						while (da2.hasNext())
						{
							LigneJournal ligne = (LigneJournal) (da2.next());
							System.out.println(ligne.getCommentaire() + " " + ligne.getObjetLienRapide() + " ");
						}
						Iterator da3 = or.getTitresAssociated();
						while (da3.hasNext())
						{
							Titre tb = (Titre) da3.next();
							System.out.println("titre de l'ordre " + tb.getNumeroTitre() + " " + tb.getSociete().getNumeroSociete() + " " + tb.getOrdre()); 
						}
						Iterator da4 = or.getTransactions();
						while (da4.hasNext())
						{
							Transaction tra = (Transaction) da4.next();
							System.out.println(" - ******** - transaction : " + tra.getNumeroTransaction() + " " + tra.getQuantiteRealisee());
						}
					}
					
				}
			
			}
		}
		Iterator test2 = cou.getLignesJournal();
		System.out.println("journal du courtier " + cou.getLogin());
		while (test2.hasNext())
		{
			LigneJournal ligne = (LigneJournal) (test2.next());
			System.out.println(ligne.getDateEvenement() + " " + ligne.getHeureEvenement() + " " + ligne.getCommentaire() + " " + ligne.getObjetLienRapide() + " " + cou);
		}
		
		}
	}
		//dan.addNewPolitique(new Politique(new Identifiant("10101000"), new Euro(10), new Euro((float)(6.09)), "cool",dan ));
	System.out.println("statut de daniel, bloque : " + dan.getBloque());
	//dan.setBloque(true);
	System.out.println("statut de daniel, bloque : " + dan.getBloque());
	
		Iterator jk = dan.getPolitiques();
		while (jk.hasNext())
		{
			Politique po = (Politique) jk.next();
			System.out.println(po.getDateDe() + " " + po.getFraisEmissionOrdre() + " " + po.getRegleFacturation() + " " + po.getSeuilLimiteLiquidites());
		}
		//dan.addNewLigneJournal(new LigneJournal(new Identifiant("669"), new Time("1234"), new Date ("1234"), new NullMoneyCurrency(),new Quantite(), false, false, "sa", "azer", "commentiare", dan));
		
		
	dan = RootData.getInstance().getCourtier(new Identifiant("Daniel"));
	
	System.out.println("statut de daniel, bloque : " + dan.getBloque());
	
		Iterator jk2 = dan.getPolitiques();
		while (jk2.hasNext())
		{
			Politique po = (Politique) jk2.next();
			System.out.println(po.getDateDe() + " " + po.getFraisEmissionOrdre() + " " + po.getRegleFacturation() + " " + po.getSeuilLimiteLiquidites());
		}
		//dan.addNewLigneJournal(new LigneJournal(new Identifiant("669"), new Time("1234"), new Date ("1234"), new NullMoneyCurrency(),new Quantite(), false, false, "sa", "azer", "commentiare", dan));
	
		System.out.println(PoliticsManager.getLastPolitiqueOfCourtier(new Identifiant("Daniel")).getDateDe());
		
		}
	
		// on peut également lancer l'objet en CORBA !
		// celui-ci est threadé et attendra...
		System.out.println("IOR de l'objet du courtier : " + new CorbaGeneric.Server().addCorbaObject(new CourtierForParticulierImplemented()));
		
	}
	
	
}
