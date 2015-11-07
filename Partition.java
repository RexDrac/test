package CVRP;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.lang.Math;
import java.awt.Point;

public class Partition
{
	private static Random random = new Random(System.currentTimeMillis());
	
	public static List<Integer> sweepPartition()
	{
		List<Integer> foodSource = new ArrayList<Integer>(300);
		List<double[]> angleArray= new ArrayList<double[]>();

		double angle,X,Y,distance;
		Point depotNode=CVRPData.getLocation(1);
		for(int i=0;i<CVRPData.NUM_NODES-1;i++)
		{
			Point customerNode=CVRPData.getLocation(i+2);
			X=customerNode.getX()-depotNode.getX();
			Y=customerNode.getY()-depotNode.getY();
			
			angle=Math.atan2(Y,X);
			distance=CVRPData.getDistance(1, i+2);
			
			double[] nodePolarCoord = new double[3];
			
			nodePolarCoord[0]=i+2;
			nodePolarCoord[1]=angle;
			nodePolarCoord[2]=distance;
			
			angleArray.add(nodePolarCoord);
			System.out.println(angle);
		}
		
		List<double[]> sortedAngleArray= new ArrayList<double[]>();
		sortedAngleArray.addAll(bubbleSort(angleArray));

		//System.out.println(sortedAngleArray.size());

		int startIndex=randomInt(0,sortedAngleArray.size());
		
		List<double[]> subList1= new ArrayList<double[]>();	
		List<double[]> subList2= new ArrayList<double[]>();
		
		subList1.addAll(sortedAngleArray.subList(0, startIndex));
		subList2.addAll(sortedAngleArray.subList(startIndex, sortedAngleArray.size()));	
		sortedAngleArray.clear();
		
		sortedAngleArray.addAll(subList2);
		sortedAngleArray.addAll(subList1);
		
		
		for(int i=0;i<sortedAngleArray.size()-1;i++)
		{
			foodSource.add((int)sortedAngleArray.get(i)[0]);
		}	
		
		return foodSource;
	}
	
	public static double randomDouble(double min,double max)
	{
		double rand=0;
		rand=min+random.nextDouble()*(max-min);//[min,max)

		return rand;
	}
	
	public static int randomInt(int min,int max)
	{
		int rand=0;
		rand=min+random.nextInt(max-min);//[min,max)
		//System.out.println(random.nextBoolean());
		return rand;
	}
	
	private static List<double[]> bubbleSort(List<double[]> source)
	{
		for(int i=0;i<source.size();i++)
		{
			for(int j=i;j<source.size();j++)
			{
				if(source.get(j)[1]<source.get(i)[1])
				{
					double[] temp=source.get(j);
					source.set(j, source.get(i));
					source.set(i, temp);
				}
			}
		}
		return source;
	}
	
	private static List<double[]> insertSort(ArrayList<double[]> source)
	{
		for(int i=0;i<source.size();i++)
		{
			for(int j=i;j>0;j--)
			{
				if(source.get(j-1)[1]>source.get(j)[1])
				{
					double[] temp=source.get(j);
					source.set(j, source.get(j-1));
					source.set(j-1, temp);
				}
			}
		}
		return source;
	}
	
	private static List<double[]> shellSort(ArrayList<double[]> source)
	{
		int gap = source.size() / 2;
		while(gap>=1)
		{
			for(int i=gap;i<source.size();i+=gap)
			{
				for(int j=i;j>0;j-=gap)
				{
					if(source.get(j-gap)[1]>source.get(j)[1])
					{
						double[] temp=source.get(j);
						source.set(j, source.get(j-gap));
						source.set(j-gap, temp);
					}
					else
						break;
				}
			}
			gap /= 2;
		}

		return source;
	}
	public static void main(String[] args) throws IOException
	{
		CVRPData.readFile("fruitybun250.vrp");
		List<Integer> L= new ArrayList<Integer>();

		L.addAll(Partition.sweepPartition());
		
		Print.printList(L);
	}
}
