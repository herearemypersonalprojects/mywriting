package Business.Courtier.Managers;

import java.util.Iterator;

import Business.Courtier.Data.CoursTitre;
import Business.Courtier.Data.RootData;
import Utils.Euro;
import Utils.ID;
import Utils.MoneyCurrency;

public class InformationBoursiereManager {

	// Permet de renvoyer le dernier cours d'une societe !
	public static MoneyCurrency getValueLastCoursOfSociety(ID numeroSociete)
	{
		CoursTitre last = null;
		MoneyCurrency data = null;
		try{
		
		Iterator it = RootData.getInstance().getSociete(numeroSociete).getCours();
		while (it.hasNext())
		{
			CoursTitre cours = (CoursTitre) it.next();
			if (last == null)
				last = cours;// le premier est toujours celui qui fait l'affaire !
			
			if (new Integer(last.getDate()).intValue() < new Integer(cours.getDate()).intValue() )
				last = cours; // on en a trouvŽ un meilleur !
			
			
			data = new Euro(new Float(last.getValeur()).floatValue());
		}
		}
		catch (Exception e)
		{
			System.out.println("*** problme pour obtenir le cours d'une sociŽtŽ ! **** : " + numeroSociete.getID());
		}
		if (last == null)
			return null;
		else
		return data;
	}
	
}
