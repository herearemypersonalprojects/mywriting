package Utils.SQL;

/**
 * @author      feduniakdaniel
 * @uml.dependency   supplier="SQL.SQLFileBatchQuery"
 * @uml.dependency   supplier="SQL.SQLStringQuery"
 */
public interface SQLQuery {

	/**
	 * @uml.property  name="query"
	 */
	public String getQuery();
	
}
