package Utils.SQL;

public class SQLUser {

	/**
	 * @uml.property  name="login"
	 */
	private String login;
	/**
	 * @uml.property  name="pass"
	 */
	private String pass;
	/**
	 * @uml.property  name="db"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SQLDB db;
	
	public SQLUser(String login, String pass, SQLDB db)
	{
		this.login = login;
		this.pass = pass;
		this.db = db;
	}
	
	public SQLDB getDB()
	{
		return this.db;
	}
	
	/**
	 * @return
	 * @uml.property  name="login"
	 */
	public String getLogin()
	{
		return this.login;
	}
	
	public String getPassword()
	{
		return this.pass;
	}
	
	// Utilisation ou non des ; dans les fichiers ddl ? 
	// uniquement pour les triggers !
	public boolean execBatchQuery (SQLQuery que, boolean semi)
	{
		return SQLExecBatchQuery.run(this, this.db, que, semi);
	}
	public boolean execBatchQuery (SQLQuery que)
	{
		return execBatchQuery(que, false);
	}
	
}
