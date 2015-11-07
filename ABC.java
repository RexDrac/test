package CVRP;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Calendar;

public class ABC
{

	private Calendar timeStart;	
	public void runABC()
	{
		timeStart=Calendar.getInstance();
		Calendar timeNow;
		int hour=0,minute=0,second=0;
		CVRPData.readFile("fruitybun250.vrp");
		System.out.println("Load File");
		System.out.println("Initialize");
		
		Bee beeColony =new Bee();
		beeColony.initializeBees();
		
		System.out.println("-----------------------");
		System.out.println("Current Lowest Cost: "+ Double.toString(beeColony.lowestCost)+"\t"+"Current Vehicle Num: "+ Integer.toString(beeColony.solutionVehicleNum));
		System.out.println("Invalid Results: "+ Integer.toString(beeColony.invalidFoodNum)+"\t"+"Alpha: "+Double.toString(Configuration.ALPHA));
		
		for(long i=0;i<Configuration.EPOCH_LIM;i++)
		{
			beeColony.employedBees();
			beeColony.onlookerBees();
			beeColony.scoutBees();
			if(i%1000==0)
				//beeColony.depotNodeArrange();
			beeColony.searchBestSolution();
			
			timeNow= Calendar.getInstance();
			
			//print computing results every 100 iterations
			if(i%1000==1)
			{
				System.out.println("-----------------------runtime: "+Integer.toString(hour)+" hours: "+Integer.toString(minute)+" minutes: "+Integer.toString(second)+" seconds:"+"----------------------------");
				System.out.println("Epoch: "+Long.toString(i)+"\t"+"Current Lowest Cost: "+ Double.toString(beeColony.lowestCost)+"\t"+"Current Vehicle Num: "+ Integer.toString(beeColony.solutionVehicleNum));
				System.out.println("Invalid Results: "+ Integer.toString(beeColony.invalidFoodNum)+"\t"+"Alpha: "+Double.toString(Configuration.ALPHA));
			}
			
			//if the runtime of the program has reached its limit, terminate program
			hour=timeNow.get(Calendar.HOUR_OF_DAY)-timeStart.get(Calendar.HOUR_OF_DAY);			
			minute=timeNow.get(Calendar.MINUTE)-timeStart.get(Calendar.MINUTE);				
			second=timeNow.get(Calendar.SECOND)-timeStart.get(Calendar.SECOND);
			if(second<0)
			{
				minute--;
				second+=60;
			}

			if(minute<0)
			{
				hour--;
				minute+=60;
			}

			if(hour*60+minute>=Configuration.RUNTIME_LIM)
			{
				System.out.println("-----------------------runtime: "+Integer.toString(hour)+" hours: "+Integer.toString(minute)+" minutes: "+Integer.toString(second)+" seconds:"+"----------------------------");
				System.out.println("Epoch: "+Long.toString(i)+"\t"+"Current Lowest Cost: "+ Double.toString(beeColony.lowestCost)+"\t"+"Current Vehicle Num: "+ Integer.toString(beeColony.solutionVehicleNum));
				System.out.println("Invalid Results: "+ Integer.toString(beeColony.invalidFoodNum)+"\t"+"Alpha: "+Double.toString(Configuration.ALPHA));
				break;
			}
		}

	}
	public static void main(String[] args) throws IOException
	{
		ABC abc = new ABC();
		abc.runABC();
	}
}
