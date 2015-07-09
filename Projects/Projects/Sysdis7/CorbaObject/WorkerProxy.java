package CorbaObject;

import CorbaGeneric.Server;

public class WorkerProxy implements WorkerOperations{

	public String IOR;
	private Worker worker=null;
	
	public WorkerProxy(String IOR)
	{
		this.IOR = IOR;
	}
	
	private void init()
	{
		if (this.worker == null)
		{
			org.omg.CORBA.Object obj = Server.orb.string_to_object(this.IOR);
			this.worker = WorkerHelper.narrow(obj);
		}
	}

	public boolean areUstilWorking() {
		this.init();
		// TODO Auto-generated method stub
		return this.worker.areUstilWorking();
	}

	public void executeJob(byte[] contentpovrayfile, int[] chunkAlreadyDone, int chunkHeight, int width, int height, int ticketnumber, int numberoffragment) {
		// TODO Auto-generated method stub
		this.init();
		this.worker.executeJob(contentpovrayfile, chunkAlreadyDone, chunkHeight, width, height, ticketnumber, numberoffragment);
	}

	public void updateJob(int numeroticket, int chunkjustdone) {
		// TODO Auto-generated method stub
		this.init();
		this.worker.updateJob(numeroticket, chunkjustdone);
	}

	public void setId(int id) {
		// TODO Auto-generated method stub
		this.init();
		this.worker.setId(id);
	}

	public void wakeUp() {
		// TODO Auto-generated method stub
		this.init();
		this.worker.wakeUp();
	}

	public void cancelTask(int ticket) {
		// TODO Auto-generated method stub
		this.init();
		this.worker.cancelTask(ticket);
	}
	

	
	

}
