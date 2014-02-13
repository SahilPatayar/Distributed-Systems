import java.net.*;
import com.sun.nio.sctp.*;
import java.nio.*;
import java.util.Map;

public class SCTPClient implements Runnable
{
	public static final int MESSAGE_SIZE = 1024;
	public static String server;
	public static int port;
	
	/*
	 * Constructor for Sctp Client
	 * 
	 * */
	
	SCTPClient(String ser, int portNo)
	{
		server = ser;
		port = portNo;		
		Thread t = new Thread(this, "Client");
		t.start();
	}	
	
	/*
	 * Over ride run method
	 * 
	 * It is reading the Hash map and sending messages to others.
	 * 
	 * */
	
	
	public void run()
	{
		int num = 32678;
		try
		{
			Thread.sleep(15000);			
		}  
		catch(InterruptedException e1)
		{			
			e1.printStackTrace();
		}
		//System.out.println("Node with process Id : " + Node.getProcessId() + "\n");
		
		
			for(Map.Entry<String, Integer> conn : Project1.config.entrySet())
			{
				makeConnection(conn.getKey(), conn.getValue(), num);
				num = num + 100;		
			}	
			System.out.println("Node " + Project1.getProcessId() + " has finished sending messages.\n");				
	}
	
	/*
	 * Method to make connection with other Machines and sending messages
	 * 
	 * */
	
	public void makeConnection(String ser, int portNo, int cPort)
	{
		ByteBuffer byteBuffer = ByteBuffer.allocate(MESSAGE_SIZE);
		String message = "";
		
		try
		{
			
			SocketAddress socketAddress = new InetSocketAddress(ser,portNo);
			
			SctpChannel sctpChannel = SctpChannel.open();
			
			sctpChannel.bind(new InetSocketAddress(cPort));
			
			sctpChannel.connect(socketAddress);
			
			MessageInfo messageInfo = MessageInfo.createOutgoing(null,0);
			
			//convert the string message into bytes and put it in the byte buffer
			int[] vec = Project1.getVector();
			
			System.out.println("Node  " + Project1.getProcessId() + " has vector before sending message  : " + FidgeMattern.vectorToString(vec) + "\n");
			
			message = FidgeMattern.createClientMessage(vec, Project1.getProcessId());			
				
			Project1.setVector(FidgeMattern.makeVector(message));
			
			System.out.println("Node " + Project1.getProcessId() + " is Sending Message to " + ser  + " : "  + message + "\n");
			
			byteBuffer.clear();

			byteBuffer.put(message.getBytes());
			
			//Reset a pointer to point to the start of buffer 
			byteBuffer.flip();
			
			//Send a message in the channel (byte format)
			sctpChannel.send(byteBuffer,messageInfo);			

			byteBuffer.clear();
			Thread.sleep(1000);
			sctpChannel.close();
			//Node.setVector(FidgeMattern.makeVector(message));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
