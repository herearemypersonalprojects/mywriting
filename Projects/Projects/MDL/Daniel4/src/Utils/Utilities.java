package Utils;

import java.util.Calendar;

public class Utilities {

	public static String getCalendarDate() 
	{
		Calendar c = Calendar.getInstance();
		Integer year = new Integer(c.get(Calendar.YEAR));
		Integer month = new Integer(c.get(Calendar.MONTH) + 1);
		Integer day = new Integer(c.get(Calendar.DAY_OF_MONTH));
		
		String y = year.toString();
		String m = month.toString();
		String d = day.toString();
		
		while (y.length() < 4) y = "0" + y;
		while (m.length() < 2) m = "0" + m;
		while (d.length() < 2) d = "0" + d;
		
		if (y.length() > 4) y = y.substring(0,4);
		if (m.length() > 2) m = m.substring(0,2);
		if (d.length() > 2) d = d.substring(0,2);
		
		return y+m+d;
		
	}
	
	public static String getCalendarTime()
	{
		// l'heure est exprimée en seconde écoulée depuis le début de la journée !
		
		Calendar c = Calendar.getInstance();
		Integer heure = new Integer(c.get(Calendar.HOUR_OF_DAY));
		Integer minute = new Integer(c.get(Calendar.MINUTE));
		Integer seconde = new Integer(c.get(Calendar.SECOND));
		
		String h = heure.toString();
		String m = minute.toString();
		String s = seconde.toString();
		
		while (h.length() < 2) h = "0" + h;
		while (m.length() < 2) m = "0" + m;
		while (s.length() < 2) s = "0" + s;
		
		if (h.length() > 2) h = h.substring(0,2);
		if (m.length() > 2) m = m.substring(0,2);
		if (s.length() > 2) s = s.substring(0,2);
		String answer = h+m+s;
		return answer;
	}
	
	
}
