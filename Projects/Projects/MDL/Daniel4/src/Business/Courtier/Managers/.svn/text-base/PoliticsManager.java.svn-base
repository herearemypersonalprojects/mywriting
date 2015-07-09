package Business.Courtier.Managers;

import java.util.Iterator;

import Business.Courtier.Data.Courtier;
import Business.Courtier.Data.LigneJournal;
import Business.Courtier.Data.Politique;
import Business.Courtier.Data.RootData;
import Utils.Date;
import Utils.Euro;
import Utils.ID;
import Utils.Identifiant;
import Utils.MoneyCurrency;

public class PoliticsManager {

	public static final String REGLEFACTURATION_ALEMISSION = "a l emission";
	public static final String REGLEFACTURATION_APRESPREMIERERECEPTION = "suite a la premiere transaction";
	
	public static boolean createNewPolitique(ID courtier, String regleFacturation, MoneyCurrency seuilLimiteLiquidites, MoneyCurrency fraisEmissionOrdre)
	{
		regleFacturation = regleFacturation.replaceAll("'", " ");
		
		// création d'une politique pour un courtier !
		// on obtient le courtier nous intéressant !
		if (regleFacturation == null) regleFacturation = REGLEFACTURATION_APRESPREMIERERECEPTION;
		
		if (seuilLimiteLiquidites == null) seuilLimiteLiquidites = new Euro((float)0.0);
		if (fraisEmissionOrdre == null) fraisEmissionOrdre =  new Euro ((float) 0.0);
		
		boolean allgood = true;
		Courtier cou  = null;
		Politique pol  = null;
		LigneJournal lig = null;
		try
		{
		Business.Courtier.DB.getDataManager.beginTransaction(); // vu qu'on est pas sûr d'arriver au bout !
		
		cou = RootData.getInstance().getCourtier(courtier);
		
		// on crée la politique
		pol = new Politique(new Identifiant(RootData.getDate().getDate()), seuilLimiteLiquidites, fraisEmissionOrdre, regleFacturation, cou ); 
		// on l'ajoute au courtier !
		cou.addNewPolitique(pol);
		
		// on crée un ligne dans le journal pour ça !
		lig = JournalManager.createNewLigne("Nouvelle politique du courtier " + cou.getLogin() + " applicable a partir du " + pol.getDateDe(), null, null, JournalManager.TYPEOPERATION_CREATION, pol.getCourtier());
		cou.addNewLigneJournal(lig);
		
		}
		catch (Exception e)
		{
			// si il y a eu un problème !
			System.out.println("*** problème ! ***" + e.getMessage());
			Business.Courtier.DB.getDataManager.rollBack();
			cou.LignesJournal.remove(lig);
			cou.Politiques.remove(pol);
			
			allgood = false;
		}
		if (allgood)
			Business.Courtier.DB.getDataManager.commit();
		
		Business.Courtier.DB.getDataManager.endTransaction();
		
		return allgood; // renvoie si tout c'est bien passé ou non !
		
	}
	
	public static Politique getLastPolitiqueOfCourtier(ID loginCourtier)
	{
		// donne la dernière politique d'un courtier !
		Iterator it = RootData.getInstance().getCourtier(loginCourtier).getPolitiques();
		Politique answer = null;
		while (it.hasNext())
		{
			Politique pol = (Politique) it.next();
			if (answer == null) answer = pol;
			
			if (answer != null)
				if (new Integer(pol.getDateDe()).intValue() > new Integer(answer.getDateDe()).intValue()) answer = pol;
		}
		return answer;
	}
	public static Politique getPolitiquePourUneDate(ID loginCourtier, Date laDateSouhaitee)
	{
		// Retourne la politique à appliquer à un objet pour une date précise !
		Iterator it = RootData.getInstance().getCourtier(loginCourtier).getPolitiques();
		Politique answer = null;
		while (it.hasNext())
		{
			Politique pol = (Politique) it.next();
			if (answer == null && new Integer(pol.getDateDe()).intValue() <= new Integer(laDateSouhaitee.getDate()).intValue())
				answer = pol;
			if (answer != null)
				if (new Integer(pol.getDateDe()).intValue() > new Integer(answer.getDateDe()).intValue() && new Integer(pol.getDateDe()).intValue() <= new Integer(laDateSouhaitee.getDate()).intValue() )
					answer = pol;
		}
		return answer;
	}
	
}
