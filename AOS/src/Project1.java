
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Project1 
{
	public static int[] processes;           // Vector for processes
	public static SCTPServer server;		// Server for a single Machine
	public static SCTPClient client;		// Client for single machine
	public static int noOfProcess;			// Total number of processes
	private static int processId;			// Id of this process
	
	public static HashMap<String, Integer> config = new HashMap<String, Integer>();		// Hash map to store config file data 
	
	
	/*
	 * Constructor for creating a Node
	 * 1. parse the configuration file
	 * 2. Start the server
	 * 3. Start the client
	 * 
	 * */
	
	public Project1(String host, int port, String path, int processId)
	{
		setProcessId(processId);
		ParseConfigFile(path);
		
		server = new SCTPServer(host, port);
		client = new SCTPClient(host, port);
		processes = new int[noOfProcess];		
	}
	
	/*
	 * Method to get the vector
	 * 
	 * */
	
	public static int[] getVector()
	{
		return processes;
	}
	
	/*
	 * Method to set the vector
	 * 
	 * */
	
	public static void setVector(int[] newVector)
	{
		for(int i = 0; i < newVector.length ; i++)
		{
			processes[i] = newVector[i];
		}
	}
	
	/*
	 * Method to Parse the config file
	 * 
	 * */
	
	public static void ParseConfigFile(String file)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			line = reader.readLine();
			
			String[] line2 = line.split(" ");
			
			noOfProcess = Integer.parseInt(line2[0]);
			
			System.out.println("Number of Nodes : " + noOfProcess);
			line = reader.readLine();
			
			while((line = reader.readLine()) != null)
			{
				String[] tokens = line.split(" ");
				int node = Integer.parseInt(tokens[0]);
				String host = tokens[1];
				int portNo = Integer.parseInt(tokens[2]);
				
				//System.out.println("node : " + node);
				//System.out.println("Host : " + host);
				//System.out.println("Port No : " + portNo);		
				if(node == getProcessId())
				{
					continue;
				}
				else
				{
					if(!config.containsKey(host))
					{					
						config.put(host, portNo);
					}					
				}
			}
			
			reader.close();
			
			
		}
		catch(Exception e)
		{
			System.out.println("Exception in ParseConfigFile");
			e.printStackTrace();
		}		
	}	
	
	/*
	 * Method to show the HashMap Entries
	 * 
	 * */
	
	
	public static void showHash()
	{
		System.out.println("----------------------------------\n");
		System.out.println("Process Id : " + getProcessId());
		System.out.println("Number of Processes : " + processes.length);
		System.out.println("Hash Map entries : ");
		System.out.println("----------------------------------\n");
		
		
		for (Map.Entry<String, Integer> entry : config.entrySet()) 
		{
		    System.out.println("key : " + entry.getKey() + " Value : " + entry.getValue());
		}
	}	

	/*
	 * Method to get the Process's id
	 * 
	 * */
	
	public static int getProcessId()
	{
		return processId;
	}

	/*
	 * Method to set the Process's id
	 * 
	 * */
	
	public static void setProcessId(int Id)
	{
		processId = Id;
	}
	
	
	/*
	 * Main Method
	 * 
	 * */
	
	public static void main(String[] args) 
	{
		if(args.length != 4)
		{
			System.out.println("Invalid Arguments.");
			System.out.println("Use this syntax : ");
			System.out.println("Hostname portNumber pathofConfigFile processID" );
			System.exit(-1);
		}	
		
		@SuppressWarnings("unused")
		Project1 node = new Project1(args[0], Integer.parseInt(args[1]),args[2], Integer.parseInt(args[3]));
		showHash();
	}
}
 