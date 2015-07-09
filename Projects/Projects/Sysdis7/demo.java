import java.io.IOException;


public class demo {

	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		Thread stdout = null;
		Thread stderr = null;
		// Soit un exemple de traitement povray :
		String filename = "test.pov";
		
		// le mieux semble de traiter ligne apr�s ligne !
		
		int width = 480;
		int height = 240;
		
		// les donn�es ci-contre �chantillionn�es sont bonnes !
		
		int blockSize = 9;
		
		int startingRow = 1;
		int endingRow=1;
		
		while (startingRow <= height)
		{
			endingRow = startingRow + blockSize;
			
			// On v�rifie de ne pas d�passer les colonnes ni les lignes...
			if (endingRow > height) endingRow = height;
			
		// povray +Itest.pov +W480 +H240 +SR1 +ER10 +SC1 +EC10
		String[] args1 = { "/bin/sh", "-c", "povray +I" + filename + " +W" + width + " +H" + height + " +SR" + startingRow + " +ER" + endingRow + " +SC" + 1 + " +EC" + width + " +Otest" + startingRow + "_" + endingRow + ".png"  };
		String cmd=new String();
		for (int i=0; i < args1.length; i+=1)
			cmd += (args1[i].toString() + " ");
		
		System.out.println(cmd);
		
		try {
			
			process = runtime.exec(args1);
			stdout = new Thread(new ThreadedInputStreamReader(process.getInputStream()));
			stdout.start();
			stderr = new Thread(new ThreadedInputStreamReader(process.getErrorStream()));
			stderr.start();
		} catch (IOException e) {
			// Si il y a une erreur
			e.printStackTrace();
		}
		
		
		// On calcul le bloc suivant...
	
			startingRow += blockSize + 1;
		
		}
	}
	
}
