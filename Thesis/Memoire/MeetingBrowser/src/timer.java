
public class timer {
	  private long start;
	  private long end;

	  public timer() {
		    reset();
		  }

		  public void start() {
		    start = System.currentTimeMillis();
		  }

		  public void end() {
		    end = System.currentTimeMillis();
		  }

		  public long duration(){
		    return (end-start);
		  }

		  public void reset() {
		    start = 0;  
		    end   = 0;
		  }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    // simple example
	    timer Timer = new timer();
	    Timer.start();
	    for (int i=0; i < 1380; i++){ System.out.print(".");}
	    Timer.end();
	    System.out.println("\n" + Timer.duration() + " ms");
	  }
}
