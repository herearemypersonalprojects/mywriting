package Business.Courtier.Managers;

import Business.Courtier.Data.Courtier;
import Business.Courtier.Data.LigneJournal;
import Business.Courtier.Data.Particulier;
import Business.Courtier.Data.RootData;
import Utils.ID;

public class ParticulierManager {

	public static boolean addNewParticulier(ID login, boolean EstUnePersonneMorale, String NomEntreprise, String NumeroTVA, String MotDePasse, String Prenom, String Nom, ID loginCourtier)
	{
		boolean allgood = true;
	Particulier cou = null;
	LigneJournal lig = null;
	LigneJournal lig2 = null;
	
	Courtier coucou = null;
	Business.Courtier.DB.getDataManager.beginTransaction();
	
	try
	{
		if (Nom != null)
			Nom = Nom.replaceAll("'", " ");
		
		if (NomEntreprise != null)
			NomEntreprise = NomEntreprise.replaceAll("'", " ");
		
		if (NumeroTVA != null)
			NumeroTVA = NumeroTVA.replaceAll("'", " ");
		if (MotDePasse != null)
			MotDePasse = MotDePasse.replaceAll("'", " ");
		
		if (Prenom != null)
			Prenom = Prenom.replaceAll("'", " ");
		
		coucou =RootData.getInstance().getCourtier(loginCourtier);
		// on crée le particulier;
		cou = new Particulier (login, NomEntreprise, NumeroTVA, EstUnePersonneMorale, false, MotDePasse, Prenom, Nom,coucou );
		// on ajoute le particulier au courtier !
		coucou.addNewParticulier(cou);
		
		// on aimerait le dire dans une ligne journal !
		lig = JournalManager.createNewLigne("Creation d''un nouveau particulier " + cou.getLogin(), null, null, JournalManager.TYPEOPERATION_CREATION, cou);
		cou.addNewLigneJournal(lig);
		
		lig2 = JournalManager.createNewLigne("Creation d''un nouveau particulier " + cou.getLogin(), null, null, JournalManager.TYPEOPERATION_CREATION, coucou);
		coucou.addNewLigneJournal(lig2); // on le met dans le journal du courtier également !
	}
	catch (Exception e)
	{
		System.out.println("*** problème lors de l'ajout d'un particulier !*** "  + e.getMessage());
		if (coucou != null) // on supprime le particulier du courtier !
			{
				coucou.Particuliers.remove(cou);
				coucou.LignesJournal.remove(lig2);
			}
		
		if (cou != null) // on supprime la ligne de journal du particulier !
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
