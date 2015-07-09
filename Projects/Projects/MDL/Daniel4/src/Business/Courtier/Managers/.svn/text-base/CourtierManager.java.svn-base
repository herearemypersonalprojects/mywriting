package Business.Courtier.Managers;

import Business.Courtier.Data.Courtier;
import Business.Courtier.Data.LigneJournal;
import Business.Courtier.Data.RootData;
import Utils.ID;
import Utils.Identifiant;

public class CourtierManager {

	public static boolean createNewCourtier(ID login, String motDePasse, String loginBourse, String motdepassebourse)
	{
		// on pense à échapper les méchants caractères !
		if (motDePasse != null)
			motDePasse =  motDePasse.replaceAll("'", " ");
		
		if (loginBourse != null)
			loginBourse = loginBourse.replaceAll("'", " ");
		
		if (motdepassebourse != null)
			motdepassebourse = motdepassebourse.replaceAll("'", " ");
			
		boolean allgood = true;
		Courtier cou = null;
		LigneJournal lig = null;
		Business.Courtier.DB.getDataManager.beginTransaction();
		
		try
		{

			// on crée le courtier et on l'ajoute !
			cou = new Courtier (login, motDePasse, new Identifiant(loginBourse), motdepassebourse, false, RootData.getInstance());
			RootData.getInstance().addNewCourtier(cou);
			// on aimerait le dire dans une ligne journal !
			lig = JournalManager.createNewLigne("Creation d un nouveau courtier " + cou.getLogin(), null, null, JournalManager.TYPEOPERATION_CREATION, cou);
			cou.addNewLigneJournal(lig);
		}
		catch (Exception e)
		{
			System.out.println("*** problème lors de l'ajout d'un courtier !*** "  + e.getMessage());
			RootData.getInstance().Courtiers.remove(cou);
			if (cou != null)
				cou.LignesJournal.remove(lig);
			
			allgood = false;
			Business.Courtier.DB.getDataManager.rollBack();
		}
		if (allgood)
			Business.Courtier.DB.getDataManager.commit();
		
		Business.Courtier.DB.getDataManager.endTransaction();
		
		return allgood;
	}
	
}
