package Connector;

public class UDPString {

	private byte[] bytes;
	private String message;
	
	public UDPString()
	{
		bytes = new String().getBytes();
	}
	
	public UDPString (String message)
	{
		if (message.length() > UDPMulticast.MAXSIZE)
			message = message.substring(0, UDPMulticast.MAXSIZE-1);
		
		this.bytes = message.getBytes();
		this.message = message;
	}
	
	public boolean equals(Object obj)
	{
		return obj.toString().equals(this.message);
	}
	
	public String toString()
	{
		return new String (this.bytes);
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	

}
