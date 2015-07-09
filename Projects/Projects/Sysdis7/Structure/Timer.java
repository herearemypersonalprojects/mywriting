package Structure;

public class Timer {

	
	
	public Timer(long timeToWait)
	{
		try{
		Thread.sleep(timeToWait);
		
		} catch (InterruptedException e) {
			// pas important
		}
	}
	


}
