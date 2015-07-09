import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class sql {
	static String port;
	static String address;
	static String user;
	static String password;
	static String dataname;
	static Connection connect;
	public sql(){
		this.address = "pneumatix.idiap.ch";
		this.port = "3306";
		this.dataname = "bet";
		this.user = "betReader";
		this.password = "pwd4betReader";
	}
	public sql(String p, String a, String u, String pass, String d){
		this.port = p;
		this.address = a;
		this.user = u;
		this.password = pass;
		this.dataname = d;
		
		connect = null;
	}
	
	public static void closedata(){
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void connectdata() throws Exception{
	if(connect != null) return;
	Class.forName("com.mysql.jdbc.Driver");
	connect = DriverManager.getConnection("jdbc:mysql://"+
											address+":"+
											port+"/"+
											dataname,
											user, 
											password);
	}
	
	public static void getQuestions(String output)	throws Exception {
			connectdata();
			
			Statement instruction = connect.createStatement();
			/*
			SELECT *
			FROM `observations`
			WHERE meeting = 'IB4010'
			AND reject = 'A'
			ORDER BY mediaTime			
			*/
			ResultSet resultat = instruction.executeQuery("SELECT * " +
														  "FROM observations " +
														  "WHERE reject = "+'"'+"A"+'"'+
														  " and meeting = "+'"'+output+'"' +
														  " and scope !=  "+'"'+"Throughout"+'"' + 
														  " ORDER BY mediaTime");
			String filename = "questions_"+output+".txt";
			file File = new file(filename);
			
			File.deletefile(filename);
			System.out.println("Meeting: "+output);
			int No = 0;
			while(resultat.next()){						
						System.out.println("---------------------------");		
						System.out.println("Number +" + resultat.getRow());
			            System.out.println("Reject: "+resultat.getString("reject"));
			            System.out.println("Scope: " + resultat.getString("scope"));
						System.out.println("True statement: "+resultat.getString("trueStatement"));
						System.out.println("False statement: "+resultat.getString("falseStatement"));
						System.out.println("Extrait text:");
						
						
						File.append("---------------------------",filename);
						File.append(String.valueOf(++No),filename);
						File.append(resultat.getString("scope"),filename);
						File.append(resultat.getString("trueStatement"),filename);
						File.append(resultat.getString("falseStatement"),filename);
						
			}			
			closedata();
	}
	public static void main(String[] args) throws Throwable{
		sql SQL = new sql();
		//SQL.getQuestions("IS1008c");
	}
}
