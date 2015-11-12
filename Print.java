package CVRP;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Print
{
    
    public static void print() {
        print("");
    }
    
    public static void print(Object o) {
        print(o, true);
    }

    public static void print(Object o, boolean newLine) {
        if (newLine)
            System.out.println(o);
        else
            System.out.print(o);
    }

    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++)
            print(array[i]);
    }
    
    public static void printArray(Object[] array) {
        for (int i = 0; i < array.length; i++)
            print(array[i]);
    }
    
    public static void printList(List list)
    {
    	for(int i=0;i<list.size();i++)
    	{

    		if(i<(list.size()-1))
    		{
    			System.out.print(list.get(i));
    			System.out.print(",");    			
    		}
    		else
    			System.out.println(list.get(i));
    	}
    }
    
    public static void printConfig()
    {
    	
    }
    
    public static void writeResult(String fileName, String loginName, String candidateNum, String name, String description, double lowestCost, List<Integer> bestSolution)
    {
        try
        {
	    	File Text = new File(fileName);
	        FileOutputStream fos = new FileOutputStream(Text);
	        OutputStreamWriter osw = new OutputStreamWriter(fos);    
	        BufferedWriter w = new BufferedWriter(osw);
	        w.write("login "+loginName+" "+candidateNum);
	        w.newLine();
	        w.write("name "+name);
	        w.newLine();
	        w.write("algorithm "+description);
	        w.newLine();
	        w.write("cost "+ String.format("%.3f", lowestCost));
	        w.newLine();
	        
	        List<List<Integer>> vehicleRouteList = new ArrayList<List<Integer>>();
	        List<Integer> depotNodes = new ArrayList<Integer>();
			//find the position of the depot node in the list
			for(int i=0;i<bestSolution.size();i++)
			{
				if(bestSolution.get(i)==1)
					depotNodes.add(i);
			}
			for(int i=0;i<depotNodes.size()-1;i++)
			{
		        List<Integer> vehicleRoute = new ArrayList<Integer>();
				//System.out.println(depotNodes.get(i));
				vehicleRoute.addAll(bestSolution.subList(depotNodes.get(i)+1, depotNodes.get(i+1)));
				//System.out.println(vehicleRoute.size());
				vehicleRouteList.add(vehicleRoute);
				//Print.printList(vehicleRouteList.get(i));
			}
			for(int i=0;i<vehicleRouteList.size();i++)
			{
				//System.out.println(vehicleRouteList.get(i));
				w.write("1->");
				for(int j=0;j<vehicleRouteList.get(i).size();j++)
				{
					w.write(Integer.toString(vehicleRouteList.get(i).get(j))+"->");
				}
				w.write("1");
				w.newLine();
			}
	        w.close();
        }
        catch (IOException e)
        {
        	System.err.println("Problem writing to the file "+fileName);
        }    	
    }
    
	static FileInputStream inStream; // = new FileInputStream("best-solution.txt");
    static DataInputStream dStream;
    static BufferedReader reader;    
    	 
    public static ArrayList<Integer> readResult(String fileName)
    {
    	 ArrayList<Integer> route = new ArrayList<Integer>();

    	 try
    	 {
    		 inStream = new FileInputStream(fileName);
    		 dStream = new DataInputStream(inStream);
    		 reader = new BufferedReader(new InputStreamReader(inStream));

    		 String line;
    		 String delim = "->";
    		 //route.add(1);
    		 while ((line = reader.readLine()) != null)
    		 {
    			 //route = new ArrayList<Integer>();

    			 if (line.startsWith("login") || line.startsWith("name") || line.startsWith("algorithm") || line.startsWith("cost"))
    				 continue;

    			 String[] tokens = line.split(delim);
    			 for (int i = 0; i < tokens.length-1; i++)
    			 {
    				 //route.add(new Integer(tokens[i]));	
    				 route.add(Integer.valueOf(tokens[i]));
    			 }
    		 } 
    		 route.add(1);
    	 }
    	 catch (FileNotFoundException e)
    	 {
    		 System.err.println("Error: best-solution.txt not found.");
    	 }
    	 catch (IOException e)
    	 {
    		 System.err.println("Error whilst reading from best-solution.txt. Is it formatted correctly?");
    	 }
    	 return route;
    }
}
