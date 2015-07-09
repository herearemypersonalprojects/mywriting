package Utils;

public class Date {

	private String date;
	
	public Date(String date)
	{
		// attention, le format utilisé est : yyyymmdd
		// Création d'une nouvelle date !
		while (date.length() < 4 + 2 + 2) // 4 pour l'année, 2 pour le mois, 2 pour le jour !
			date = "0" + date;
		this.date = date;
	}
	
	public int getYear()
	{
		return new Integer(this.date.substring(0, 4)).intValue();
	}
	
	public int getMonth()
	{
		return new Integer(this.date.substring(4, 6)).intValue();
	}
	
	public int getDay()
	{
		return new Integer(this.date.substring(6, 8)).intValue();
	}
	
	public String getDate()
	{
		// retourne la date considérée
		return this.date;
	}
}
