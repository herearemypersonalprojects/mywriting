package Business.Courtier.Data;

import Utils.ID;
import Utils.MoneyCurrency;

public class Liquidation {

	private Courtier courtier;
	private ID datede;
	private ID autrecourtier;
	private boolean dejaeffectue;
	private MoneyCurrency montant;
	
	public Liquidation(ID datede, ID autrecourtier, boolean ejaeffectue, MoneyCurrency ontant, Courtier courtier) {
		// Création de la liquidation considérée...
		
		this.courtier = courtier;
		this.datede = datede;
		this.autrecourtier = autrecourtier;
		this.montant = ontant;
		this.dejaeffectue = ejaeffectue;
	}

	
	public Courtier getCourtier()
	{
		// retourne le courtier considérer par la liquidation :
		return this.courtier;
	}
	
	public String getMontant()
	{
		// retourne le montant de la liquidation à effectuer :
		return this.montant.getAmount();
	}
	
	public boolean getEstDejaEffectue()
	{
		// retourne si la liquidation a déjà été cochée par le courtier !
		return this.dejaeffectue;
	}
	
	public String getDateDe()
	{
		// retourne la date de la liquidation considérée
		return this.datede.getID();
	}
	
	public String getAutreCourtier()
	{
		// retourne la valeur de l'autre courtier considérer
		return this.autrecourtier.getID();
	}

	
	// La seul méthode "set" à considérer porte sur estdéjàeffectue...
	public void setEstDejaEffectue(boolean value)
	{
		Business.Courtier.DB.getDataManager.ExecuteToDB("update COU_liquidation set EstEffectue='" + new String(value + "").toUpperCase() + "' where DateDe=" + this.getDateDe() + " and AutreCourtier='" + this.getAutreCourtier() + "'");
		this.dejaeffectue = value;
	}
	
}
