package Business.Courtier.Managers;

import java.util.Iterator;

import Utils.Date;
import Utils.Identifiant;
import Utils.MoneyCurrency;
import Utils.NullMoneyCurrency;
import Utils.Quantite;
import Business.Courtier.Data.LigneJournal;
import Business.Courtier.Data.Ordre;
import Business.Courtier.Data.RootData;


public class JournalManager {


	public static final String TYPEOPERATION_CREATION = "Creation";
	public static final String TYPEOPERATION_MODIFICATION = "Modification";

	
	private static long numerodeligne = 0;
	
	public static long getNewNumeroDeLigne()
	{
		// on gros on regarde combien de ligne il y a déjà dans le journal,
		// si le numéro est plus grand que numéro de ligne, alors numérodeligne = ce qui est trouvé,
		// on ajoute un au tout, on le renvoie
		long tps = 0;
		String cool = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select count(numeroligne) from COU_LigneJournal", "count(numeroligne)");
		if (cool != null)
		{
			tps = new Long(cool).longValue();
		}
		if (tps > numerodeligne) numerodeligne = tps;
		
		numerodeligne +=1;
		
		return numerodeligne;
	}
	
	public static LigneJournal createNewLigne(String Commentaire, MoneyCurrency montant, Quantite quantite, String typeoperation, Object onObject)
	{
		if (montant == null) montant = new NullMoneyCurrency();
		if (quantite == null) quantite = new Quantite();
		if (Commentaire ==  null) Commentaire = "";
		
		Commentaire =  Commentaire.replaceAll("'", " ");
		
		if (onObject == null)
			throw new NullPointerException("onObject must not be null !");
		// permet de renvoyer une ligne journal sur une base rapide !
		return new LigneJournal(new Identifiant("" + getNewNumeroDeLigne()), RootData.getInstance().getTime(), RootData.getInstance().getDate(), montant,quantite, false, false, typeoperation, onObject.getClass().getName(), Commentaire,  onObject );
	}
	
	public static Date getDateCreation(Ordre or)
	{
		// retourne la date de création de l'ordre or !
		if (or == null) throw new NullPointerException("Object must not be null !");
		
			Iterator it = or.getLignesJournal();
			boolean found = false;
			Date answer = null;
			while (it.hasNext() && !found)
			{
				LigneJournal lig = (LigneJournal) it.next();
				if (lig.getTypeOperation().equals(TYPEOPERATION_CREATION))
				{
					found = true;
					answer = new Date(lig.getDateEvenement());
				}
			}
			return answer;
	}
}
