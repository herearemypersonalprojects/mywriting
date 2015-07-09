package Business.Courtier.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import Utils.SQL.SQLDB;
import Utils.SQL.SQLQuery;
import Utils.SQL.SQLStringQuery;
import Utils.SQL.SQLUser;

public class getDataManager {

	// Hardcoding values about Database from courtier...
	public static SQLDB db = null;
	public static SQLUser usr = null;
	public static String prefix = null;
	public static boolean DBactive = false; // Say that db is not active...
	public static boolean DBverbose = true; // Say if we have to print lot of data or not !
	
	private final static String prefixERAUsed = "COU_";
	
	public static Connection conn; // A connection to the DataBase
	
	public static void refresh()
	{
		ExecuteToDB("select login from COU_courtier");
	}
	
	public static void beginTransaction()
	{
		if (DBactive)
		try {
			refresh();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void endTransaction()
	{
		if (DBactive)
		try {
			refresh();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void rollBack()
	{
		if (DBactive)
			try {
				refresh();
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void commit()
	{
		if (DBactive)
		try {
			refresh();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void ExecuteToDB(String request)
	{
		ExecuteToDB(request, null);
	}
	
	public static String ExecuteSingleToDB(String request, String fieldName)
	{
		Iterator it = ExecuteToDB(request, fieldName).iterator();
		if (!it.hasNext())
			return null;
		else
			return it.next().toString();
	}
	
	
	public static ArrayList ExecuteToDB(String request, String fieldName)
	{
		// request = the request to send to the DB
		ArrayList answer = new ArrayList();
		if (!DBactive)
			return answer; // Don't have to ask the DB, this is not running !
		
		if (db == null || usr == null || prefix == null)
			throw new NullPointerException("getDataManager:Invalid SQL Data to access the DB");
		
		
		if (request == null)
			throw new NullPointerException("getDataManager:too poor data to make the SQL request !");
		
		// récupère toute l'information disponible pour le résultat de la requête sur un champ !
		
		// Making the SFW request
	
		SQLQuery que = new SQLStringQuery( request.replaceAll(prefixERAUsed, prefix));
		
		try {
			// Setting up JDBC Driver for Oracle;
			
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); // Driver JDBC pour Oracle
			if (conn == null || conn.isClosed())
			{
				// Setting up conn to be right;
				conn = DriverManager.getConnection("jdbc:oracle:thin:@" + usr.getDB().getHostIP() + ":" + usr.getDB().getHostPORT() + ":" + usr.getDB().getDBName() ,usr.getLogin(),usr.getPassword());
				
			}
			
			// Creating statement on this connection...
			Statement stmt = conn.createStatement();
			// Getting the result...
			if (DBverbose)
			System.out.println("Executing to DB : " + que.getQuery().toString());
			
			ResultSet rsl = stmt.executeQuery(conn.nativeSQL(que.getQuery().toString()));
			
			while (rsl.next() && fieldName != null)
			{
				String dat = rsl.getString(fieldName); // Getting the each-line result
				if (dat != null)
					answer.add(dat);
			}
			
			stmt.close();
			//conn.close(); // on ne ferme pas la connexion, histoire d'aller beaucoup, beaucoup plus vite !
			} catch (SQLException e) {
				throw new RuntimeException("ExecuteToDB(" + request + ":" + fieldName + ");" +e);
				
			}
		
		
		return answer;
	}
	
	
	
	public static void constructDB()
	{
		if (!DBactive)
			return ; // Don't have to go further...
		
		if (db == null || usr == null || prefix == null)
			throw new NullPointerException("getDataManager:Invalid SQL Data to access the DB");
		
		if(new Build(usr).make(prefix))
		{
			System.out.println("Base de donnees courtier correctement construite !");
		}
		else
		{
			System.out.println("Probleme lors de la construction de la base de donnees du courtier");
		}
		
	}
	
	public static void destroyDB()
	{
		if (!DBactive)
			return ; // Don't have to go further...
		
		if (db == null || usr == null || prefix == null)
			throw new NullPointerException("getDataManager:Invalid SQL Data to access the DB");
		
		if(new Destroy(usr).make(prefix))
		{
			System.out.println("Base de donnees courtier correctement detruite !");
		}
		else
		{
			System.out.println("Probleme lors de la destruction de la base de donnees du courtier");
		}
	}
	
	
}
