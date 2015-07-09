package Structure;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class Utils {

	public static int quinconceA(int x, int taillemax)
	{
		if (taillemax > 0)
		{
		// soit nous avons déjà x
		int niveau = 0;
		boolean trouve = false;
		while (!trouve)
		{
			if (((int)x < Math.pow(2, niveau)))
			{
				trouve = true;
			}
			else
			{
				niveau += 1;
			}
		}
		// ici, on a trouvé le niveau de l'objet;
		// il faut calculer l'avancement par rapport à 0
		int avancement = (int) (taillemax / (Math.pow(2, niveau)));
		//System.out.println("Element : " + x + " niveau : " + niveau + " avancement : " + avancement);
		
		// on calcul le premier indice du niveau considéré ;
		int first=0;
		if (niveau >= 1)
		{
			// si le  niveau est le 0 alors, first est 0... c'est logique, donc on ne doit pas le traiter !
			first = (int) Math.pow(2, (niveau-1));
		}
		//System.out.println("First du niveau " + niveau + " : " + first );
		//System.out.println("Difference : " + (x-first) );
		//System.out.println("Avancement inter x : " + avancement * 2);
		// il suffit de calculer la différence entre x et le first pour obtenir sa valeur finale;
		if (x > 0)
			{
				int answer = (avancement + ((x - first) * avancement * 2) );
				if (answer == 0 && x != 0) 
				{
					// ici, en fait le nombre proposé n'est pas dans les bornes;
					// c'est quand il y a trop de workers pour une tâche précise...
					// on peut essayer un nombre au hasard... ça peut toujours etre bien !
					return x % taillemax; // là on est plus ou moins sûr de bien distribuer !
				}
				else
					return answer; // on peut renvoyer le calcul ! 
			}
		else
			return 0; // et oui, si c'était 0, alors la position initiale est 0 !
		}
		else
			return 0; // ici la taille est considéré comme invalide, mais ce n'est pas grave !
	}
	
	public static int quinconceB(int x, int taillemax)
	{
		return (taillemax-1)- quinconceA(x, taillemax);
	}
	
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
		
		return new Integer(seconde + 60*minute + 60*60*heure).toString();
	}
	
	
	public static byte[] getContentOfFile(String fileName) {
        File file = new File(fileName);
        byte buffer[] = new byte[(int)file.length()];
        try{
            BufferedInputStream input = new
                BufferedInputStream(new FileInputStream(fileName)); 
            input.read(buffer,0,buffer.length);
            input.close();            
        }catch(Exception e){
            System.out.println("File ClientsImpl error: "+e.getMessage());
            e.printStackTrace();
        }
        return(buffer);
	}
	
	public static void writeContentToFile(byte[] content, String filename)
	// Permet de copier le contenu d'un String dans un fichier...
	{
		BufferedOutputStream output;
		try {
		
			output = new BufferedOutputStream(new FileOutputStream(filename));
		
		output.write(content,0,content.length);
		output.flush();
		output.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getPrintScreenWhatDone(boolean [] chunkAlreadyDone)
	{
//		 on peut rapidement l'imprimer à l'écran l'état d'avancement d'une tâche !
		
		String screen = new String();
		for (int i=0; i < chunkAlreadyDone.length; i+=1)
		{
			if (chunkAlreadyDone[i])
				screen += 1;
			else
				screen += 0;
		}
		return(screen);
	}
	
	
}
