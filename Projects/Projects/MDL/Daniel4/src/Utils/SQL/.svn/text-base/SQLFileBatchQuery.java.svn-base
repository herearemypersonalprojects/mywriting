package Utils.SQL;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;

public class SQLFileBatchQuery implements SQLQuery {
	
	static public Vector domain = new Vector(); // Dictinonaire de couple afin d'Žvaluer les domaines...
	// Utilisation statique permettant de rŽappliquer les dŽfinitions dans d'autres fichiers.
	
	/**
	 * @uml.property  name="que"
	 */
	private String que; // La valeur textuelle de la Query
	
	public SQLFileBatchQuery(InputStream data)
	{
		this.que = new String(); // instanciation de la query
		
		BufferedReader lecteurAvecBuffer = null;
	    String ligne;

	    try
	    {
	    	lecteurAvecBuffer = new BufferedReader(new InputStreamReader(data));
	     	while ((ligne = lecteurAvecBuffer.readLine()) != null)
	    	{
	    		ligne = ligne.toUpperCase(); // Mise en majuscule
	    		if (ligne.startsWith("CREATE DOMAIN")) // Le domaine doit �tre retenu dans le dictionnaire
	    		{
	    			String Domain = ligne.substring("CREATE DOMAIN".length() + 1, ligne.length());
	    			String nom = Domain.substring(0,Domain.indexOf(" "));
	    			String reste = Domain.substring(Domain.indexOf(" "), Domain.length());
	    			// Nous devons nettoyer reste !
	    			boolean doneSmth = true;
	    			while (doneSmth)
	    			{
	    				doneSmth = false;
	    				if (reste.startsWith(" ")) 
	    				{
	    					reste = reste.substring(1, reste.length());
	    					doneSmth = true;
	    				}
	    				if (reste.startsWith("AS ")) 
	    				{	
	    					reste = reste.substring(2, reste.length());
	    					doneSmth = true;
	    				}
	    			}
	    			reste = reste.replaceAll(";", "");
	    			domain.add(new couple(nom, reste));
	    		}
	    			
	    		else
	    			if (!ligne.startsWith("CREATE DATABASE")) // Sous Oracle, �a ne passe pas non plus !
	    			{
	    				if (!(ligne.equals("") || ligne.equals("\n")))
	    					this.que += ligne+'\n';
	    			}
	    	}
		
	    // Remplacement des nom de domaine par leur traduction 	
	     	
		Iterator it = domain.iterator();
		while (it.hasNext())
		{
			couple c = (couple) it.next();
			que = que.replaceAll(c.getFirst().toString(), c.getLast().toString());
		}
		lecteurAvecBuffer.close();
		} catch (IOException e) {
			// Cela ne devrait jamais arriver !
			e.printStackTrace();
		}
	}
	
	public String getQuery() 
	{
		return this.que;
	}

}
