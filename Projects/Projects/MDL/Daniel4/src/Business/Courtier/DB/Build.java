package Business.Courtier.DB;

import Utils.SQL.SQLFileBatchQuery;
import Utils.SQL.SQLStringQuery;
import Utils.SQL.SQLUser;

public class Build {

	
	/**
	 * @uml.property  name="usr"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SQLUser usr;
	
	public Build (SQLUser usr)
	{
		this.usr = usr;
	}
	public boolean make(String prefix)
	{
		boolean complete = true;
		// D'abord les domaines puis les tables puis les procŽdures puis les triggers; => attention il y a un fichier par triggers !
		if (!this.usr.execBatchQuery(new SQLFileBatchQuery (Build.class.getResourceAsStream("DOMAIN.ddl")))) complete = false;
		
		Utils.SQL.SQLFileBatchQuery.domain.add(new Utils.SQL.couple("COU_",prefix));
		
		if (!this.usr.execBatchQuery(new SQLFileBatchQuery (Build.class.getResourceAsStream("CONSTRUCTOR.ddl")))) complete = false;
		
		return complete;
	}
}
