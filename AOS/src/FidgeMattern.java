
/*
 * Implemented Fidge Mattern's Vector Clock Protocol
 *  
 * */

public class FidgeMattern
{
	int[] old;
	int[] newV;
	
	/*
	 * Constructor for FidgeMattern's Vector clock protocol
	 * 
	 * */
	
	public FidgeMattern(String message)
	{	
		 old = Project1.getVector();
		 newV = makeVector(message);	
	}
	
	/*
	 * Method to convert String to vector
	 * 
	 * */ 
	public static int[] makeVector(String message)
	{
		String[] tokens = message.split(" ");
		
		int len = Project1.noOfProcess;		

		int[] vector = new int[len];
		int j = 0;
		for(int i = 0; i < tokens.length; i++)
		{			
			if(tokens[i].equals("[") )
			{				
				continue;
			}
			 if(tokens[i].contains("]"))
			{
				//System.out.println("here in ]");
				break;
			}
			
				//System.out.println("getting : " + tokens[i]);	
				vector[j] = Integer.parseInt(tokens[i]);
				j++;
			
		}
		
		
		
		
		for(int i = 0; i < vector.length; i++)
		{
			//System.out.println("Vector value at " + i + " : " + vector[i]);
		}
		return vector;
	}
	
	/*
	 * Method to compare two vectors and get maximum out of them
	 * 
	 * */
	
	public static int[] compareVectors(int[] vector1, int[] vector2, int processId)
	{
		if(vector1.length != vector2.length)
		{
			System.out.println("Vectors are not equal in length");	
			System.exit(-1);
		}
		
		for(int i = 0; i < vector1.length; i++)
		{
			if(i == processId)
				continue;
			else
			{
				if(vector1[i] <= vector2[i])
				{
					vector1[i] = vector2[i];
				}
			}
		}
		vector1[processId]++; 
		
		return vector1;
	}
	
	/*
	 * Method to Convert Vector to String
	 * 
	 * */	
	
	public static String vectorToString(int[] vector)
	{
		String vec = "[ ";
		for(int i = 0; i < vector.length; i++)
		{
			vec+= vector[i] + " ";
			
		}
		vec+= "]";
		
		//System.out.println("vector to String : " + vec);
		return vec;
	}
	
	
	/*
	 * Method to create a message for client
	 * 
	 * it updates the vector according to process id
	 * 
	 * */	
	
	public static String createClientMessage(int[] vector, int process)
	{
		vector[process]++;
		
		String vec = vectorToString(vector);
		return vec;
		
	}	

}
