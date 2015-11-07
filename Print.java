package CVRP;
import java.util.List;

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
}
