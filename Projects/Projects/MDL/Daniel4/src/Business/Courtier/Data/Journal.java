package Business.Courtier.Data;

import Utils.Date;
import Utils.Euro;
import Utils.ID;
import Utils.MoneyCurrency;
import Utils.NullMoneyCurrency;
import Utils.Quantite;
import Utils.Time;

public class Journal {

	// Méthode statique permettant de construire automatiquement une ligne du journal à partir du numéro de la ligne !
	public static LigneJournal constructLigneJournal(ID numerodeligne, Object lienrapide)
	{
		// il faut : heureevenement, montant, quantite, typeoperation, surtypeobjet, dateevenement, dejaconsulteparparticulier, dejaconsulteparcourtier, commentaire
		String heureevenement = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select heureevenement from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "heureevenement" );
		String montant = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select montant from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "montant" );
		String quantite = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select quantite from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "quantite" );
		String typeoperation = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select typeoperation from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "typeoperation" );
		String surtypeobjet = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select surtypeobjet from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "surtypeobjet" );
		String dateevenement = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select dateevenement from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "dateevenement" );
		String dejaconsulteparparticulier = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select dejaconsulteparparticulier from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "dejaconsulteparparticulier" );
		String dejaconsulteparcourtier = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select dejaconsulteparcourtier from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "dejaconsulteparcourtier" );
		String commentaire = Business.Courtier.DB.getDataManager.ExecuteSingleToDB("select commentaire from COU_lignejournal where numeroligne='" + numerodeligne.getID()+"'", "commentaire" );
		
		boolean ejaconsulteparparticulier = dejaconsulteparparticulier.equals("TRUE");
		boolean ejaconsulteparcourtier = dejaconsulteparcourtier.equals("TRUE");
		
		Time eureevenement = new Time(heureevenement);
		Date ateevenement = new Date(dateevenement);
		
		// il faut prendre en compte les champs facultatifs !
		MoneyCurrency ontant= null;
		
		if (montant == null)
		{
			// pas de montant spécifié !
			ontant = new NullMoneyCurrency(); 
		}
		else
		{
			// un montant a été spécifié !
			ontant = new Euro(new Float(montant).floatValue()); 
		}
		Quantite antite=null;
		if (quantite == null)
		{
			// pas de quantité spécifiée !
			antite = new Quantite();
		}
		else
		{
			antite = new Quantite(new Integer(quantite).intValue());
		}
		
		
		return new LigneJournal(numerodeligne, eureevenement, ateevenement, ontant, antite, ejaconsulteparparticulier, ejaconsulteparcourtier, typeoperation, surtypeobjet, commentaire, lienrapide);
	}
	
}
