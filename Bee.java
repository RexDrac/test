/**
 * Bee.java
 *
 * This class contains operators necessary for the Artificial Bee Colony Algorithm
 * access the demand and distance information. It does some
 * parameter checking to make sure that nodes passed to the get
 * methods are within the valid range.
 *
 * 
 *
 * @author Chuanyu Yang
 * @version 30/10/2015
 */
package CVRP;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

public class Bee
{
	private List<Food> foodSourceList = new ArrayList<Food>();
	private List<Integer> bestSolution= new ArrayList<Integer>();
	public int solutionVehicleNum=0;
	public double lowestCost = 10000000;
	private static Random random = new Random(System.currentTimeMillis());
	public int invalidFoodNum=0;
	private double fitnessSum=0;
	
	public void initializeBees()
	{
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			Food foodSource= new Food();
			foodSource.initializeFood(CVRPData.NUM_NODES);
			this.foodSourceList.add(foodSource);
		}
	}
	
	public void employedBees(/*List<Food> foodSourceList*/)
	{
		//number of food source equals to the number of employed bees
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			List<Integer> neighborFoodSource=new ArrayList<Integer>();
			neighborFoodSource.addAll(Operator.combined(foodSourceList.get(i).foodSource));//use combined neighboring operators
			
			double cost =Food.calCost(neighborFoodSource);
			double violateCapacity=Food.calViolateCapacity(neighborFoodSource);
			double fitness=Food.calFitness(cost, violateCapacity);
			
			//if neighbor foodSource is better than original food source, replace it
			if(fitness>foodSourceList.get(i).fitness)
			{
				foodSourceList.get(i).setFoodSource(neighborFoodSource);
			}
			else
			{
				foodSourceList.get(i).iteration+=1;
			}
		}
		//adjust the factor of the amount of capacity violated
		calInvalidFoodNum();
		if(invalidFoodNum>=Configuration.EMPLOYED_BEE_NUM/2)
		{
			Configuration.ALPHA+=0.5;
		}
		else if(invalidFoodNum<Configuration.EMPLOYED_BEE_NUM/3)
		{
			Configuration.ALPHA-=0.5;
			if(Configuration.ALPHA<0.5)
				Configuration.ALPHA=0.5;
		}
	}
	
	public void onlookerBees(/*List<Food> foodSourceList*/)
	{

		calFitnessSum(foodSourceList);
		int index=0;// index of food source in the list
		for(int i=0;i<Configuration.ONLOOKER_BEE_NUM;i++)
		{
			index= rouletteWheel(/*foodSourceList*/);
			//System.out.println(index);
			ArrayList<Integer> newFoodSource = new ArrayList<Integer>();
			newFoodSource.addAll(Operator.combined(foodSourceList.get(index).foodSource));
			//Print.printList(newFoodSource);
			foodSourceList.get(index).neighborFoodSource.add(newFoodSource);

		}
/*		
		//roulette wheel		
		double wheelSize=0.0;		
		wheelSize=fitnessSum;

		for(int i=0;i<Configuration.ONLOOKER_BEE_NUM;i++)
		{
			double pocketStart=0.0;
			double pocketEnd=0.0;
			double ball=0.0;
			ball=Bee.randomDouble(0,wheelSize);
			
			for(int j=0;j<Configuration.EMPLOYED_BEE_NUM;j++)
			{
				pocketEnd=pocketStart+foodSourceList.get(j).fitness;
				if(ball>=pocketStart&&ball<=pocketEnd)
				{
					ArrayList<Integer> newFoodSource = new ArrayList<Integer>();
					newFoodSource.addAll(foodSourceList.get(j).combined());
					foodSourceList.get(j).neighborFoodSource.add(newFoodSource);
					//end loop when found a food source from the roulette wheel
					break;
				}
				pocketStart=pocketEnd;
			}
		}
*/		
		
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			//continue searching if neighborhood is empty
			if(foodSourceList.get(i).neighborFoodSource.size()<=0)
			{
				foodSourceList.get(i).neighborFoodSource.clear();
				foodSourceList.get(i).iteration+=1;
				continue;				
			}
			
			double fitness=0;
			double cost=0;
			double violateCapacity=0;
			
			double bestFitness=0;
			List<Integer> betterFoodSource= new ArrayList<Integer>();			
		
			for(int j=0;j<foodSourceList.get(i).neighborFoodSource.size();j++)
			{
				List<Integer> tempFoodSource = new ArrayList<Integer>();
				tempFoodSource.addAll(foodSourceList.get(i).neighborFoodSource.get(j));
				cost=Food.calCost(tempFoodSource);
				violateCapacity=Food.calViolateCapacity(tempFoodSource);
				fitness=Food.calFitness(cost, violateCapacity);
				
				if(fitness>bestFitness)
				{
					betterFoodSource.clear();
					bestFitness=fitness;
					betterFoodSource.addAll(foodSourceList.get(i).neighborFoodSource.get(j));					
				}
			}
			if(bestFitness>foodSourceList.get(i).fitness)
			{
				foodSourceList.get(i).setFoodSource(betterFoodSource);				
			}
			else
			{
				foodSourceList.get(i).iteration+=1;
				foodSourceList.get(i).neighborFoodSource.clear();
			}

		}
		
		calInvalidFoodNum();
		if(invalidFoodNum>=Configuration.EMPLOYED_BEE_NUM/2)
		{
			Configuration.ALPHA+=0.5;
		}
		else if(invalidFoodNum<Configuration.EMPLOYED_BEE_NUM/3)
		{
			Configuration.ALPHA-=0.5;
			if(Configuration.ALPHA<0.5)
				Configuration.ALPHA=0.5;
		}		
		
	}
	public void scoutBees(/*List<Food> foodSourceList*/)
	{
		int maxIteration=0;
		double bestFitness=0;
		int targetIndex=0;
		int sourceIndex=0;
		
		for(int i=0;i<foodSourceList.size();i++)
		{
			//if food source has not been changed for certain amount of iteration, replace the food source with
			//the overall best solution
			List<Integer> newFoodSource = new ArrayList<Integer>();
			if(foodSourceList.get(i).iteration>Configuration.ITERATION_LIM)
			{
				newFoodSource.addAll(Operator.combined(this.bestSolution));
				foodSourceList.get(i).setFoodSource(newFoodSource);
				//changed in 2015/11/6 23:45
				
				if(random.nextBoolean())
				{
					Food.insertQueenNode(foodSourceList.get(i).foodSource);					
				}
				else
				{
					Food.removeQueenNode(foodSourceList.get(i).foodSource);
				}
				
			}
			/*
			if(foodSourceList.get(i).iteration>maxIteration)
			{
				targetIndex=i;
				maxIteration=foodSourceList.get(i).iteration;
			}
			//search for the food source with the current best fitness
			if(foodSourceList.get(i).fitness>bestFitness)
			{
				sourceIndex=i;
				bestFitness=foodSourceList.get(i).fitness;
			}
			*/
		}
		//List<Integer> newFoodSource = new ArrayList<Integer>();
		//newFoodSource.addAll(Operator.combined(foodSourceList.get(targetIndex).foodSource));
		//foodSourceList.get(targetIndex).setFoodSource(newFoodSource);
		
	}
	
	public void depotNodeArrange()
	{
		//number of food source equals to the number of employed bees
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			List<Integer> neighborFoodSource=new ArrayList<Integer>();
			if(foodSourceList.get(i).violateCapacity>0)
				neighborFoodSource.addAll(Food.insertQueenNode(foodSourceList.get(i).foodSource));
			else
				neighborFoodSource.addAll(Food.removeQueenNode(foodSourceList.get(i).foodSource));
			
			double cost =Food.calCost(neighborFoodSource);
			double violateCapacity=Food.calViolateCapacity(neighborFoodSource);
			double fitness=Food.calFitness(cost, violateCapacity);
			
			//if neighbor foodSource is better than original food source, replace it
			if(fitness>foodSourceList.get(i).fitness)
			{
				foodSourceList.get(i).setFoodSource(neighborFoodSource);
			}
			else
			{
				foodSourceList.get(i).iteration+=1;
			}
		}
		//adjust the factor of the amount of capacity violated
		calInvalidFoodNum();
		if(invalidFoodNum>=Configuration.EMPLOYED_BEE_NUM/2)
		{
			Configuration.ALPHA+=0.5;
		}
		else if(invalidFoodNum<Configuration.EMPLOYED_BEE_NUM/3)
		{
			Configuration.ALPHA-=0.5;
			if(Configuration.ALPHA<0.5)
				Configuration.ALPHA=0.5;
		}
	}
	
	public int rouletteWheel(/*List<Food> foodSourceList*/)
	{
		int foodSourceIndex=0;
		//roulette wheel
		double wheelSize=fitnessSum;

		double pocketStart=0.0;
		double pocketEnd=0.0;
		double ball=0.0;
		ball=Bee.randomDouble(0,wheelSize);
		//System.out.println(ball);
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			pocketEnd=pocketStart+foodSourceList.get(i).fitness;
			if(ball>=pocketStart&&ball<=pocketEnd)
			{
				foodSourceIndex=i;
				//end loop when found a food source from the roulette wheel
				break;
			}
			pocketStart=pocketEnd;
		}
		
		return foodSourceIndex;
	}
	
	public List<Integer> searchBestSolution(/*List<Food> foodSourceList*/)
	{
		double cost = 0.0;
		double violateCapacity=0;
		for(int i=0;i<foodSourceList.size();i++)
		{
			cost=foodSourceList.get(i).cost;
			violateCapacity=foodSourceList.get(i).violateCapacity;
			if(cost<this.lowestCost&&violateCapacity<=0)
			{
				this.lowestCost=cost;
				this.bestSolution.clear();
				this.bestSolution.addAll(foodSourceList.get(i).foodSource);
				this.solutionVehicleNum=Food.countVehicleNum(foodSourceList.get(i).foodSource);
			}
		}
		
		return bestSolution;
	}
	
	public int calInvalidFoodNum(/*List<Food> foodSourceList*/)
	{
		this.invalidFoodNum=0;
		for(int i=0;i<foodSourceList.size();i++)
		{
			if(foodSourceList.get(i).violateCapacity>0)
			{
				this.invalidFoodNum+=1;
			}
		}
		return this.invalidFoodNum;
	}
	
	//calculate the total wheel size of the roulette wheel
	public double calFitnessSum(List<Food> foodSourceList)
	{
		double sum=0.0;
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			sum+=foodSourceList.get(i).fitness;
		}
		fitnessSum=sum;
		return sum;
	}
	
	public static double randomDouble(double min,double max)
	{
		double rand=0;
		rand=min+random.nextDouble()*(max-min);//[min,max)

		return rand;
	}	
	public static void main(String[] args) throws IOException
	{
		CVRPData.readFile("fruitybun250.vrp");
		Bee bees = new Bee();
		bees.initializeBees();
		System.out.println("Initialize");		
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			//Print.printList(bees.foodSourceList.get(i).foodSource);
			
			System.out.println(Integer.toString(i)+"\tCost\t"+Double.toString(bees.foodSourceList.get(i).cost)
					+"\tViolated Capacity\t"+Double.toString(bees.foodSourceList.get(i).violateCapacity));
		}
		bees.employedBees();
		System.out.println("employedBees");
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			//Print.printList(bees.foodSourceList.get(i).foodSource);
			System.out.println(Integer.toString(i)+"\tCost\t"+Double.toString(bees.foodSourceList.get(i).cost)
					+"\tViolated Capacity\t"+Double.toString(bees.foodSourceList.get(i).violateCapacity)
					+"\titeration\t"+Integer.toString(bees.foodSourceList.get(i).iteration));
		}	
		System.out.println(bees.invalidFoodNum);
		
		bees.onlookerBees();
		System.out.println("onlookerBees");
		for(int i=0;i<Configuration.EMPLOYED_BEE_NUM;i++)
		{
			//Print.printList(bees.foodSourceList.get(i).foodSource);
			System.out.println(Integer.toString(i)+"\tCost\t"+Double.toString(bees.foodSourceList.get(i).cost)
					+"\tViolated Capacity\t"+Double.toString(bees.foodSourceList.get(i).violateCapacity)
					+"\titeration\t"+Integer.toString(bees.foodSourceList.get(i).iteration));
		}	
		System.out.println(bees.invalidFoodNum);
		bees.scoutBees();
	}
}
