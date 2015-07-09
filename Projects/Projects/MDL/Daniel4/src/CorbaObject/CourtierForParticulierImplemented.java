package CorbaObject;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.Date;
import Utils.Identifiant;

import sun.jvmstat.monitor.Monitor;
import Business.Courtier.Data.CompteLiquidites;
import Business.Courtier.Data.CompteTitres;
import Business.Courtier.Data.RootData;
import Business.Courtier.Data.SocieteCotee;
import Business.Courtier.Managers.InformationBoursiereManager;

public class CourtierForParticulierImplemented extends
		CourtierForParticulierPOA {
	
	
	public Evolution ConsulterBourse(String nomSociete) {
		// TODO Auto-generated method stub
		return null;
	}

	public Succes EmettreOrdre(String loginCourtier, String loginParticulier, Ordre unordre) {
		// TODO Auto-generated method stub
		return null;
	}

	public Succes TransfererLiquidite(String loginCourtier, Virement unvirement, String loginParticulier) {
		// TODO Auto-generated method stub
		return null;
	}

	public Succes TransfererTitre(String loginCourtier, String loginParticulier, String nomSociete, int nombreTitre, String numeroCompteDebiteur, String numeroCompteCrediteur) {
		// TODO Auto-generated method stub
		return null;
	}

	public Succes checkLogin(String loginCourtier, String loginParticulier, String motDePasse) {
		// TODO Auto-generated method stub
		Succes answer = null;
		synchronized(this)
		{
			try
			{
				Business.Courtier.Data.Particulier part = RootData.getInstance().getCourtier(new Identifiant(loginCourtier)).getParticulier(new Identifiant(loginParticulier));
				if (part.getMotDePasse().equals(motDePasse))
				{
					answer = new Succes(true, "Le particulier " + loginParticulier + " est correctement identifié");
					
				}
				else
				{
					answer = new Succes(false, "Le particulier " + loginParticulier + " n'est pas correctement identifié, mot de passe incorrect !");
				}
			}
			catch(Exception e)
			{
				System.out.println("problème checking login & pass of particulier : " + e.getMessage());
				return null;
			}
		}
		
		return answer;
	}

	public CompteDeLiquidite[] getListCompteLiquidite(String loginCourtier, String numeroParticulier) {
		// TODO Auto-generated method stub
		CompteDeLiquidite[] answer = null;
		synchronized(this)
		{
			try
			{
				ArrayList data = new ArrayList();
				Iterator it = RootData.getInstance().getCourtier(new Identifiant(loginCourtier)).getParticulier(new Identifiant(numeroParticulier)).getComptesLiquidites();
				while (it.hasNext())
				{
					Business.Courtier.Data.CompteLiquidites liq = (Business.Courtier.Data.CompteLiquidites) it.next();
					CompteDeLiquidite tps = new CompteDeLiquidite(liq.getNumCompteLiquidites(), new Float(liq.getSoldeCourant()).floatValue(), new Float(liq.getSoldeDisponible()).floatValue());
					data.add(tps);
				}
				
				answer = new CompteDeLiquidite[data.size()];
				for (int i =0; i < answer.length; i+=1)
				{
					answer[i] = (CompteDeLiquidite) data.get(i);
				}
			}
			catch (Exception e)
			{
				System.out.println("erreur obtenir le compte de liquidites d'un particulier " + e.getMessage());
				return null;
			}
		}
		return answer;
	}

	public CompteDeTitre[] getListCompteTitre(String loginCourtier, String numeroParticulier, String numeroCompteDeLiquidites) {
		// TODO Auto-generated method stub
		CompteDeTitre[] dan = null;
		synchronized(this)
		{
			try
			{
			ArrayList answer = new ArrayList();
			ArrayList lotofsociete = new ArrayList();
			Business.Courtier.Data.CompteLiquidites compte = RootData.getInstance().getCourtier(new Identifiant(loginCourtier)).getParticulier(new Identifiant(numeroParticulier)).getCompteLiquidites(new Identifiant(numeroCompteDeLiquidites));
			Iterator da = compte.getCompteTitres();
			while (da.hasNext())
			{
				Business.Courtier.Data.CompteTitres tit = (Business.Courtier.Data.CompteTitres) da.next();
				Iterator titres = tit.getTitres();
				// soit on doit constuire la liste des societe pour ensuite ajouter les titres !
				ArrayList societes = new ArrayList();
				while (titres.hasNext())
				{
					Business.Courtier.Data.Titre gr = (Business.Courtier.Data.Titre) titres.next();
					if (!societes.contains(gr.getSociete()))
					{
						societes.add(gr.getSociete()); 
						// on construit donc ici la liste des différentes sociétés !
					}	
				}
				// après cela on peut itérer sur l'ensemble des sociétés du vecteur pour constuire la liste des titres !
				Iterator seconde = societes.iterator();
				while (seconde.hasNext())
				{
					Business.Courtier.Data.SocieteCotee society = (Business.Courtier.Data.SocieteCotee) seconde.next();
					ArrayList listedestitresiciconcernes = new ArrayList();
					Iterator waw = tit.getTitres();
					while (waw.hasNext())
					{
						Business.Courtier.Data.Titre lol = (Business.Courtier.Data.Titre) waw.next();
						if (lol.getSociete() == society)
						{
							// le titre sélectionné appartient à la société visée,
							// on l'ajoute à la liste des titres concernés
							listedestitresiciconcernes.add(lol);
						}
					}
					Titre[] atcha = new Titre[listedestitresiciconcernes.size()];
					for (int i=0; i < atcha.length; i+=1)
					{
						atcha[i] = new Titre(((Business.Courtier.Data.Titre)listedestitresiciconcernes.get(i)).getNumeroTitre(),society.getNumeroSociete());
					}
					// super on a créé l'entrée pour la société !
					Societe tps = new Societe(society.getNumeroSociete(), society.getNomSociete(), atcha, new Float(InformationBoursiereManager.getValueLastCoursOfSociety(new Identifiant(society.getNumeroSociete())).getAmount()).floatValue(),society.getBloquee());
					lotofsociete.add(tps);
				}
				Societe[] cla = new Societe[lotofsociete.size()];
				for (int i=0; i < cla.length; i+=1)
				{
					cla[i] = (Societe)lotofsociete.get(i);
				}
				// il faut égalment obtenir tous les ordres d'un compte de titres !
				ArrayList ordreaplacer = new ArrayList();
				Iterator ua = tit.getOrdres();
				while (ua.hasNext())
				{
					Business.Courtier.Data.Ordre or = (Business.Courtier.Data.Ordre) ua.next();
					// soit on a l'ordre !
					Ordre ora = new Ordre(
							or.getCompteTitres().getCompteLiquidites().getParticulier().getLogin(),
							or.getEstUnAchat(),
							or.getEstAtoutPrix(),
							or.getSociete().getNumeroSociete(),
							new Integer(or.getQuantiteDesiree()).intValue(),
							new Integer(or.getQuantiteRealisee()).intValue(),
							new Float(or.getMontantLimite()).floatValue(),
							new DateStruct(
									new Date(or.getDateDebut()).getYear(),
									new Date(or.getDateDebut()).getMonth(),
									new Date(or.getDateDebut()).getDay(),
									0,
									0,
									0,
									0
								),
							new DateStruct(
									new Date(or.getDateButoir()).getYear(),
									new Date(or.getDateButoir()).getMonth(),
									new Date(or.getDateButoir()).getDay(),
									0,
									0,
									0,
									0
								),
							new DateStruct(),
							or.getEtat() ); // eu il y a une date ??? que dire que dire ?
					// l'ordre en enfin construit !
					ordreaplacer.add(ora);
				}
				Ordre[] wyp = new Ordre[ordreaplacer.size()];
				for (int i=0; i < wyp.length ; i+=1)
				{
					wyp[i] = (Ordre) ordreaplacer.get(i);
				}
				
				answer.add(new CompteDeTitre(tit.getNumCompteTitres(),cla, wyp ));
			}
			
			dan = new CompteDeTitre[answer.size()];
			for (int i=0; i < dan.length; i+=1)
			{
				dan[i] = (CompteDeTitre) answer.get(i);
			}
			
			
			
			}
			catch (Exception e)
			{
				System.out.println("erreur ! Obtenir les comptes de liquidites d'un particulier ! " + e.getMessage());
				return null;
			}
		}
		return dan;
	}

	public Societe[] getListSociete() {
		// TODO Auto-generated method stub
		Societe[] dan = null;
		synchronized (this)
		
		{
			try{
			ArrayList answer = new ArrayList();
			Iterator it = RootData.getInstance().getSocietesCotees();
			while (it.hasNext())
			{
				SocieteCotee soc = (SocieteCotee) it.next();
				ArrayList <Titre> titre = new ArrayList();
				Iterator it2 = soc.getTitres();
				while (it2.hasNext())
				{
					Business.Courtier.Data.Titre tit = (Business.Courtier.Data.Titre) it2.next();
					titre.add(new Titre(tit.getNumeroTitre(), tit.getSociete().getNumeroSociete()));
				}
			
				Titre[] da = new Titre[titre.size()];
				for (int i=0; i < da.length; i+=1)
				{
					da[i] = titre.get(i);
				}
				
				answer.add(new Societe(soc.getNumeroSociete(), soc.getNomSociete(), da, new Float(InformationBoursiereManager.getValueLastCoursOfSociety(new Identifiant(soc.getNumeroSociete())).getAmount()).floatValue(), soc.getBloquee()));
			
			
			}
			dan = new Societe[answer.size()];
			for (int i=0; i < answer.size(); i+=1)
			{
				dan[i] = (Societe) answer.get(i);
			}
			}
			catch (Exception e)
			{
				System.out.println("erreur getListSociete " + e.getMessage());
				return null;
			}
		}
		
		return dan;
	}

	public Societe[] getListSocietePourUnParticulier(String loginCourtier, String numeroParticulier) {
		// TODO Auto-generated method stub
		synchronized (this) // soit un appel bloquant !
		{
			// il va falloir regarder tous les titres que possède un particulier et donc prendre la liste des sociétés concernées !
		}
		
		return null;
	}

	public String[] getListCourtier() {
		// TODO Auto-generated method stub
		return null;
	}

	public Ordre[] getListOrdre(String numeroCompteTitre, DateStruct date) {
		// TODO Auto-generated method stub
		return null;
	}

	public Virement[] getListVirement(String numeroCompteLiquidite, DateStruct date) {
		// TODO Auto-generated method stub
		return null;
	}

	// soit ici nous avons un objet pour faire les différents appels !
	
}
