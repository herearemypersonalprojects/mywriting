import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ThreadedInputStreamReader implements Runnable {

	private InputStream inputStream;
	
	public ThreadedInputStreamReader (InputStream inputStream)
	{
		// Constructor : Permet de récupérer le flux de sortie d'erreur d'un process externe
		this.inputStream = inputStream;
	}
	
	public void run() 
	{
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));
		String line = new String();
		try 
		{
			while((line = reader.readLine()) != null) 
			{
				// Traitement du flux de sortie de l'application si besoin est
				// là on fait seulement un sysout pour l'instant
				System.out.println(line);
			}
		} 
		catch(IOException ioe) 
		{
			// Erreur lors de la lecture...
			ioe.printStackTrace();
		}
		finally 
		{
			try 
			{
				reader.close();
			} 
			catch (IOException e) 
			{
				// Erreur lors de la fermeture du flux
				e.printStackTrace();
			}
		}
		
	}
	

}
