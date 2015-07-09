package Business.Courtier.Managers;

import Business.Courtier.Data.Courtier;
import Business.Courtier.Data.LigneJournal;
import Business.Courtier.Data.Particulier;
import Business.Courtier.Data.RootData;
import Utils.ID;

public class UsersManager {

	
	// gestion des blocage, déblocage des courtiers, des particuliers !
	public static boolean bloqueCourtier(ID loginCourtier, String Commentaire)
	{
		
		boolean allgood = true;
		LigneJournal lig = null;
		Courtier cou = null;
		Business.Courtier.DB.getDataManager.beginTransaction();
		try
		{
			Commentaire = Commentaire.replaceAll("'", " ");
			cou = RootData.getInstance().getCourtier(loginCourtier);
			if (cou.getBloque())
				throw new RuntimeException("Le courtier " + loginCourtier.getID() + " est deja bloque !");
			
			cou.setBloque(true);
			
			lig = JournalManager.createNewLigne("Blocage du courtier " + loginCourtier.getID() + ", cause : " + Commentaire, null, null, JournalManager.TYPEOPERATION_MODIFICATION, cou);
			cou.addNewLigneJournal(lig);
		}
		catch (Exception e)
		{
			System.out.println("*** problème lors du blocage du courtier *** " + e.getMessage());
			allgood = false;
			Business.Courtier.DB.getDataManager.rollBack();
			if (cou != null)
			{
				
				cou.LignesJournal.remove(lig);
			}
		}
		if (allgood)
			Business.Courtier.DB.getDataManager.commit();
		
		Business.Courtier.DB.getDataManager.endTransaction();
		return allgood;
	}
	
	public static boolean debloqueCourtier(ID loginCourtier, String Commentaire)
	{
		boolean allgood = true;
		LigneJournal lig = null;
		Courtier cou = null;
		Business.Courtier.DB.getDataManager.beginTransaction();
		try
		{
			Commentaire = Commentaire.replaceAll("'", " ");
			cou = RootData.getInstance().getCourtier(loginCourtier);
			if (!cou.getBloque())
				throw new RuntimeException("Le courtier " + loginCourtier.getID() + " est deja debloque !");
			
			cou.setBloque(false);
			
			lig = JournalManager.createNewLigne("Deblocage du courtier " + loginCourtier.getID() + ", cause : " + Commentaire, null, null, JournalManager.TYPEOPERATION_MODIFICATION, cou);
			cou.addNewLigneJournal(lig);
		}
		catch (Exception e)
		{
			System.out.println("*** problème lors du déblocage du courtier *** " + e.getMessage());
			
			allgood = false;
			Business.Courtier.DB.getDataManager.rollBack();
			if (cou != null)
			{
				
				cou.LignesJournal.remove(lig);
			}
		}
		if (allgood)
			Business.Courtier.DB.getDataManager.commit();
		
		Business.Courtier.DB.getDataManager.endTransaction();
		return allgood;
	}
	public static boolean bloqueParticulier(ID loginParticulier, String Commentaire, ID loginCourtier)
	{
		boolean allgood = true;
		LigneJournal lig = null;
		Courtier cou = null;
		Particulier part = null;
		Business.Courtier.DB.getDataManager.beginTransaction();
		try
		{
			Commentaire = Commentaire.replaceAll("'", " ");
			cou = RootData.getInstance().getCourtier(loginCourtier);
			
			part = cou.getParticulier(loginParticulier);
			
			if (part.getBloque())
				throw new RuntimeException("Le particulier " + loginParticulier.getID() + " est deja bloque !");
			
			part.setBloque(true);
			
			lig = JournalManager.createNewLigne("Blocage du particulier " + loginParticulier.getID() + ", cause : " + Commentaire, null, null, JournalManager.TYPEOPERATION_MODIFICATION, part);
			part.addNewLigneJournal(lig);
		}
		catch (Exception e)
		{
			System.out.println("*** problème lors du blocage du particulier *** " + e.getMessage());
			allgood = false;
			Business.Courtier.DB.getDataManager.rollBack();
			if (part != null)
			{
				
				part.LignesJournal.remove(lig);
			}
		}
		if (allgood)
			Business.Courtier.DB.getDataManager.commit();
		
		Business.Courtier.DB.getDataManager.endTransaction();
		return allgood;
	}
	
	public static boolean debloqueParticulier(ID loginParticulier, String Commentaire, ID loginCourtier)
	{
		boolean allgood = true;
		LigneJournal lig = null;
		Courtier cou = null;
		Particulier part = null;
		Business.Courtier.DB.getDataManager.beginTransaction();
		try
		{
			Commentaire = Commentaire.replaceAll("'", " ");
			cou = RootData.getInstance().getCourtier(loginCourtier);
			
			part = cou.getParticulier(loginParticulier);
			
			if (!part.getBloque())
				throw new RuntimeException("Le particulier " + loginParticulier.getID() + " est deja debloque !");
			
			part.setBloque(false);
			
			lig = JournalManager.createNewLigne("Deblocage du particulier " + loginParticulier.getID() + ", cause : " + Commentaire, null, null, JournalManager.TYPEOPERATION_MODIFICATION, part);
			part.addNewLigneJournal(lig);
		}
		catch (Exception e)
		{
			System.out.println("*** problème lors du déblocage du particulier *** " + e.getMessage());
			allgood = false;
			Business.Courtier.DB.getDataManager.rollBack();
			if (part != null)
			{
			
				part.LignesJournal.remove(lig);
			}
		}
		if (allgood)
			Business.Courtier.DB.getDataManager.commit();
		
		Business.Courtier.DB.getDataManager.endTransaction();
		return allgood;
	}
	
}
