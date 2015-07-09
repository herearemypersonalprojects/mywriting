package Utils;

public class Time {

	private String heure;
	
	public Time(String heure)
	{
		this.heure = heure;
		while (this.heure.length() < 6 ) this.heure = "0" + this.heure;
 	}
	
	public String getTime()
	{
		return this.heure;
	}
}
