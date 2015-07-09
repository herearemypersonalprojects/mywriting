package Business.Courtier.Data;

import Utils.Date;
import Utils.ID;
import Utils.MoneyCurrency;
import Utils.Time;

public class CoursTitre {

	private Date date;
	private Time coursheurede;
	private SocieteCotee societe;
	private MoneyCurrency val;

	public CoursTitre(Date date, Time coursheurede, SocieteCotee numso, MoneyCurrency val) {
		// TODO Auto-generated constructor stub
		this.date = date;
		this.coursheurede = coursheurede;
		this.societe = numso;
		this.val = val;
	}
	
	public String getDate()
	{
		// retourne la date de ce cours
		return this.date.getDate();
	}
	public String getHeure()
	{
		// retourne l'heure de ce cours 
		return this.coursheurede.getTime();
	}
	
	public String getValeur()
	{
		// retourne la valeur du cours
		return this.val.getAmount();
	}
	
	public SocieteCotee getSociete()
	{
		return this.societe;
	}

}
