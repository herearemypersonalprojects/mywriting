package Business.Courtier.DB;

import Utils.SQL.SQLFileBatchQuery;
import Utils.SQL.SQLUser;

public class Destroy {

	/**
	 * @uml.property  name="usr"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SQLUser usr;

	public Destroy (SQLUser usr)
	{
		this.usr = usr;
	}
	
	public boolean make(String prefix)
	{
		boolean complete = true;
		if (!this.usr.execBatchQuery(new SQLFileBatchQuery (Build.class.getResourceAsStream("DOMAIN.ddl")))) complete = false;
		
		Utils.SQL.SQLFileBatchQuery.domain.add(new Utils.SQL.couple("COU_",prefix));
		
		if (!this.usr.execBatchQuery(new SQLFileBatchQuery (Build.class.getResourceAsStream("DESTRUCTOR.ddl")))) complete = false;
		return complete;
		
	}
	
}
