package Utils.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Vector;

public class SQLExecBatchQuery {

	/** 
	 * @uml.property name="error"
	 */
	public static String error = "";
	private static Connection conn;
	
	public static boolean DBverbose = true;
	
	public static boolean run(SQLUser usr, SQLDB db, SQLQuery query)
	{
		return run(usr, db, query, false);
	}
	
	public static boolean run(SQLUser usr, SQLDB db, SQLQuery query, boolean useSemiColon)
	{
		boolean complete = true;
		Vector subque = new Vector();
		String que = query.getQuery();
	
		// But diviser la query en sub-query le plus souvent possible
		// Oracle n'aime pas la d�finition de longues constructions !
		
		if (!useSemiColon) // Si l'on ne d�sire pas ins�rer les ";"
		{
			while (que.indexOf(";") > 0)
			{
				String sub = que.substring(0, que.indexOf(";"));
				subque.add(new String (sub));
				que = que.substring(que.indexOf(";") + 1);
			}
		}
		else
		subque.add (new String (que)); // On consid�re la query compl�te
	
		Iterator it = subque.iterator();
		while (it.hasNext()) 
		{
			try
			{
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); // Driver JDBC pour Oracle
				if (conn == null)
					conn = DriverManager.getConnection("jdbc:oracle:thin:@" + db.getHostIP() + ":" + db.getHostPORT() + ":" + db.getDBName(),usr.getLogin(),usr.getPassword());
				Statement stmt = conn.createStatement();
				String qerystring = it.next().toString();
				if (DBverbose)
				System.out.println(qerystring);
				stmt.execute(conn.nativeSQL(qerystring));
				stmt.close();
				//conn.close();
			} catch (Exception e) 
			{ 
				if (!e.toString().contains("ORA-01408: such column list already indexed"))
				{
					// Lorsqu'un identifiant est d�clar� sur une colonne,
					// un index est automatiquement cr�� sur celui-ci
					// d�s lors il est logique de recevoir ce genre d'erreur
					// et c'est pour cela qu'elle n'est pas prise en compte !
					
					// Cependant ici l'erreur est plus importante, on le signale
					System.out.println(e.toString());
					// on met l'erreur dans une variable statique afin d'acc�der � son contenu
					error = e.getMessage();
					complete =false;
				}
				
    		}
		}	
		return complete; // retourne true si tout c'est bien pass� !
	}
	
	
}
