package Utils.SQL;

public class SQLStringQuery implements SQLQuery {

	/**
	 * @uml.property  name="que"
	 */
	private String que;
	
	public SQLStringQuery(String ligne)
	{
		//ligne = ligne.toUpperCase();
		ligne = ligne.replace(";", "");
		this.que = ligne;
	}
	
	public String getQuery() {
		// TODO Auto-generated method stub
		return this.que;
	}

}
