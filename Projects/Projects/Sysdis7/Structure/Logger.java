/**
 * 
 */
package Structure;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author FŽduniak Daniel greatly inspired from Nicolas Otjaques
 *
 */
public class Logger {
	private final org.apache.log4j.Logger log;
	private static boolean init = false;
	
	private Logger(org.apache.log4j.Logger l){
		this.log = l;
	}
	
	
	public static Logger getLogger(Class c){
		if(!init){
			PropertyConfigurator.configure (DATA.LOG4GPROPERTIESFILE);
			init = true;
		}
		org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(c);
		return new Logger(log);
	}
	
	
	public void info(String msg){
		if(log.isInfoEnabled())log.info(msg);
	}
	
	public void debug(String msg){
		if(log.isDebugEnabled())log.debug(msg);
	}
	
	public void error(String msg){
		log.error(msg);
	}
	public void error(String msg, Throwable t){
		log.error(msg,t);
	}
	
	public void warn(String msg){
		log.warn(msg);
	}
	
	public void trace(String msg){
		if(log.isTraceEnabled())log.trace(msg);
	}
}
