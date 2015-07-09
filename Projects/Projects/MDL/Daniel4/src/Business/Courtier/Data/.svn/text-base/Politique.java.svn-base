package Business.Courtier.Data;

import Utils.ID;
import Utils.MoneyCurrency;

public class Politique {

	// Une politique ne sait pas être modifier, on ne peut qu'en créer une nouvelle !
	
	private ID datede;
	private MoneyCurrency seuillimiteliquidites;
	private MoneyCurrency fraisemissionordre;
	private String reglefacturation;
	private Courtier courtier;

	public Politique(ID datede, MoneyCurrency euillimiteliquidites, MoneyCurrency raisemissionordre, String reglefacturation, Courtier courtier) {
		this.datede = datede;
		this.seuillimiteliquidites = euillimiteliquidites;
		this.fraisemissionordre = raisemissionordre;
		this.reglefacturation = reglefacturation;
		this.courtier = courtier;
	}
	
	public String getDateDe()
	{
		// retourne la date de départ de la politique :
		return this.datede.getID();
	}
	
	public String getSeuilLimiteLiquidites()
	{
		// retourne le seuil limite qu'un compte de liquidites ne peut dépasser en suivant la politique
		return this.seuillimiteliquidites.getAmount();
	}
	
	public String getFraisEmissionOrdre()
	{
		// retourne la valeur des frais lors de l'émission des ordre !
		return this.fraisemissionordre.getAmount();
	}

	public String getRegleFacturation()
	{
		// retourne la façon dont le débit de la facturation opère !
		return this.reglefacturation;
	}
	
	public Courtier getCourtier()
	{
		// renvoie le courtier lié
		return this.courtier;
	}
}
