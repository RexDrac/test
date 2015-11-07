package CVRP;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

public class Food
{
	/**cost of a food source */
	public double cost=0.0;
	/**fitness of a food source */
	public double fitness=1.0;
	/** amount of capacity that is violated*/
	public double violateCapacity=0.0;
	public boolean violateCapacityFlag=false;
	/**total number of vehicles used */
	public int vehicleNum=0;
	public int iteration =0;

	public List<ArrayList<Integer> > neighborFoodSource=new ArrayList<ArrayList<Integer> >();
	private static Random random = new Random(System.currentTimeMillis());
	public List<Integer> foodSource = new ArrayList<Integer>(CVRPData.VEHICLE_CAPACITY*2);//LinkedList has no initial capacity, ArrayList does
	
	public static int randomInt(int min,int max)
	{
		int rand=0;
		rand=min+random.nextInt(max-min);//[min,max)
		//System.out.println(random.nextBoolean());
		return rand;
	}
	
	public boolean initializeFood(int size)
	{
		List<Integer> node = new ArrayList<Integer>();
		List<Integer> foodSource = new ArrayList<Integer>();
		
		int usedCapacity=0;
		for(int i=0;i<size;i++)
		{
			node.add(i+1);//node number starts from 1;
		}
		foodSource.add(1);//depot node is depicted as node 1
		
		int i=1;// first node is depot node, allocation starts from the second customer node
		do
		{
			int index = randomInt(1,node.size());//first node is depot node
			int nodeNum;
			nodeNum=node.get(index);
			if((usedCapacity+CVRPData.getDemand(nodeNum)+Configuration.CAPACITY_REDUNDANCY)<CVRPData.VEHICLE_CAPACITY)
			{
				foodSource.add(node.get(index));
				node.remove(index);
				usedCapacity+=CVRPData.getDemand(nodeNum);
				i+=1;
			}
			else
			{
				foodSource.add(1);//assign another vehicle if the former vehicle is full
				usedCapacity=0;//reset
			}
		}while(i<size);
		/*
		for(int i=1;i<size;i++)
		{
			int index = randomInt(1,node.size());//first node is depot node
			this.foodSource.add(node.get(index));
			node.remove(index);
		}
		*/
		foodSource.add(1);//depot node is depicted as node 1

		setFoodSource(foodSource);
		
		return true;
	}
	/*
	public List<Integer> swap()
	{
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(this.foodSource);
		
		while(true)
		{
			int i1=0,i2=0,element1=0,element2=0;
			i1=randomInt(1,foodSource.size()-1);//first and last elements are start nodes, exclude
			do
			{
				i2=randomInt(1,foodSource.size()-1);				
			}
			while(i1==i2);//keep finding until i1!=i2
			
			//System.out.println(i1);
			//System.out.println(i2);
			//do not allow depot node to be swapped
			if(foodSource.get(i1)==1||foodSource.get(i2)==1)//if any of the selected node are depot nodes, find a new swap set
				continue;	
			element1=foodSource.get(i1);
			element2=foodSource.get(i2);
			
			foodSource.set(i1,element2);
			foodSource.set(i2,element1);		
			break;
		}
		return foodSource;
	}
	
	public List<Integer> swapSubsequence()
	{
		List<Integer> invalidIndex = new ArrayList<Integer>();//index that should not be chosen
		List<Integer> indexList = new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource=new ArrayList<Integer>();
		foodSource.addAll(this.foodSource);//Copy the value into local variables
		
		do//find four diverse index that contains depot nodes
		{
			index=randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node already taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			}

			
		}while(indexList.size()<4);
		
		Collections.sort(indexList);
		
		List<Integer> subList1=new ArrayList<Integer>();
		List<Integer> subList2=new ArrayList<Integer>();
		List<Integer> subList3=new ArrayList<Integer>();
		List<Integer> subList4=new ArrayList<Integer>();
		List<Integer> subList5=new ArrayList<Integer>();
		
		subList1.addAll(foodSource.subList(0, indexList.get(0)));
		subList2.addAll(foodSource.subList(indexList.get(0), indexList.get(1)+1));
		subList3.addAll(foodSource.subList(indexList.get(1)+1, indexList.get(2)));
		subList4.addAll(foodSource.subList(indexList.get(2), indexList.get(3)+1));
		subList5.addAll(foodSource.subList(indexList.get(3)+1, foodSource.size()));
		
		foodSource.clear();
		foodSource.addAll(subList1);
		foodSource.addAll(subList4);
		foodSource.addAll(subList3);
		foodSource.addAll(subList2);
		foodSource.addAll(subList5);
		
		return foodSource;
	}
	
	public List<Integer> insert()
	{
		List<Integer> indexList=new ArrayList<Integer>();
		List<Integer> invalidIndex=new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(this.foodSource);
		
		do
		{
			index=randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node foodn taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			
			}
			
		}while(indexList.size()<2);
		Collections.sort(indexList);
		if(random.nextBoolean())//randomly generate 0 or 1
		{
			List<Integer> subList1=new ArrayList<Integer>();
			List<Integer> subList2=new ArrayList<Integer>();
			List<Integer> subList3=new ArrayList<Integer>();
			int element;
			
			subList1.addAll(foodSource.subList(0,indexList.get(0)));
			element=foodSource.get(indexList.get(0));
			subList2.addAll(foodSource.subList(indexList.get(0)+1,indexList.get(1)));
			subList3.addAll(foodSource.subList(indexList.get(1),foodSource.size()));
			
			foodSource.clear();
			foodSource.addAll(subList1);
			foodSource.addAll(subList2);
			foodSource.add(element);
			foodSource.addAll(subList3);
		}
		else
		{
			List<Integer> subList1=new ArrayList<Integer>();
			List<Integer> subList2=new ArrayList<Integer>();
			List<Integer> subList3=new ArrayList<Integer>();
			int element;
			
			subList1.addAll(foodSource.subList(0,indexList.get(0)));
			subList2.addAll(foodSource.subList(indexList.get(0),indexList.get(1)));
			element=foodSource.get(indexList.get(1));			
			subList3.addAll(foodSource.subList(indexList.get(1)+1,foodSource.size()));
			
			foodSource.clear();
			foodSource.addAll(subList1);
			foodSource.add(element);			
			foodSource.addAll(subList2);
			foodSource.addAll(subList3);
		}
		return foodSource;
	}
	
	public List<Integer> insertSubsequence()
	{
		List<Integer> indexList=new ArrayList<Integer>();
		List<Integer> invalidIndex=new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(this.foodSource);
		
		do
		{
			index=randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node foodn taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			
			}
			
		}while(indexList.size()<3);
		Collections.sort(indexList);
		if(random.nextBoolean())//randomly generate 0 or 1
		{
			List<Integer> subList1=new ArrayList<Integer>();
			List<Integer> subList2=new ArrayList<Integer>();
			List<Integer> subList3=new ArrayList<Integer>();
			List<Integer> subList4=new ArrayList<Integer>();
			
			subList1.addAll(foodSource.subList(0,indexList.get(0)));
			subList2.addAll(foodSource.subList(indexList.get(0),indexList.get(1)+1));//target swap subsequence
			subList3.addAll(foodSource.subList(indexList.get(1)+1,indexList.get(2)));
			subList4.addAll(foodSource.subList(indexList.get(2),foodSource.size()));
			
			foodSource.clear();
			foodSource.addAll(subList1);
			foodSource.addAll(subList3);
			foodSource.addAll(subList2);
			foodSource.addAll(subList4);
		}
		else
		{
			List<Integer> subList1=new ArrayList<Integer>();
			List<Integer> subList2=new ArrayList<Integer>();
			List<Integer> subList3=new ArrayList<Integer>();
			List<Integer> subList4=new ArrayList<Integer>();
			
			subList1.addAll(foodSource.subList(0,indexList.get(0)));
			subList2.addAll(foodSource.subList(indexList.get(0),indexList.get(1)));
			subList3.addAll(foodSource.subList(indexList.get(1),indexList.get(2)+1));//target swap subsequence
			subList4.addAll(foodSource.subList(indexList.get(2)+1,foodSource.size()));
			
			foodSource.clear();
			foodSource.addAll(subList1);
			foodSource.addAll(subList3);
			foodSource.addAll(subList2);
			foodSource.addAll(subList4);
		}		
		return foodSource;
	}
	
	public List<Integer> reverseSubsequence()
	{
		List<Integer> indexList=new ArrayList<Integer>();
		List<Integer> invalidIndex=new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(this.foodSource);
		
		do
		{
			index=randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node foodn taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			
			}
			
		}while(indexList.size()<2);
		Collections.sort(indexList);
		
		List<Integer> subList = new ArrayList<Integer>();
		
		subList.addAll(foodSource.subList(indexList.get(0), indexList.get(1)+1));
		
		Collections.reverse(subList);
		for(int i=indexList.get(0);i<=indexList.get(1);i++)
		{
			foodSource.set(i, subList.get(i-indexList.get(0)));
		}
		return foodSource;
	}
	
	public List<Integer> swapReverseSubsequence()
	{
		List<Integer> invalidIndex = new ArrayList<Integer>();//index that should not be chosen
		List<Integer> indexList = new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(this.foodSource);
		
		do//find four diverse 
		{
			index=randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node foodn taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			}

			
		}while(indexList.size()<4);
		
		Collections.sort(indexList);
		
		List<Integer> subList1=new ArrayList<Integer>();
		List<Integer> subList2=new ArrayList<Integer>();
		List<Integer> subList3=new ArrayList<Integer>();
		List<Integer> subList4=new ArrayList<Integer>();
		List<Integer> subList5=new ArrayList<Integer>();
		
		subList1.addAll(foodSource.subList(0, indexList.get(0)));//[0,get(0))
		subList2.addAll(foodSource.subList(indexList.get(0), indexList.get(1)+1));//[get(0),get(1)]
		Collections.reverse(subList2);
		subList3.addAll(foodSource.subList(indexList.get(1)+1, indexList.get(2)));
		subList4.addAll(foodSource.subList(indexList.get(2), indexList.get(3)+1));
		Collections.reverse(subList4);
		subList5.addAll(foodSource.subList(indexList.get(3)+1, foodSource.size()));
		
		foodSource.clear();
		foodSource.addAll(subList1);
		foodSource.addAll(subList4);
		foodSource.addAll(subList3);
		foodSource.addAll(subList2);
		foodSource.addAll(subList5);
		return foodSource;
	}
	
	public List<Integer> insertReverseSubsequence()
	{
		List<Integer> indexList=new ArrayList<Integer>();
		List<Integer> invalidIndex=new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(this.foodSource);
		
		do
		{
			index=randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node foodn taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			
			}
			
		}while(indexList.size()<3);
		Collections.sort(indexList);
		if(random.nextBoolean())//randomly generate 0 or 1
		{
			List<Integer> subList1=new ArrayList<Integer>();
			List<Integer> subList2=new ArrayList<Integer>();
			List<Integer> subList3=new ArrayList<Integer>();
			List<Integer> subList4=new ArrayList<Integer>();
			
			subList1.addAll(foodSource.subList(0,indexList.get(0)));
			subList2.addAll(foodSource.subList(indexList.get(0),indexList.get(1)+1));//target swap subsequence
			Collections.reverse(subList2);
			subList3.addAll(foodSource.subList(indexList.get(1)+1,indexList.get(2)));
			subList4.addAll(foodSource.subList(indexList.get(2),foodSource.size()));
			
			foodSource.clear();
			foodSource.addAll(subList1);
			foodSource.addAll(subList3);
			foodSource.addAll(subList2);
			foodSource.addAll(subList4);
		}
		else
		{
			List<Integer> subList1=new ArrayList<Integer>();
			List<Integer> subList2=new ArrayList<Integer>();
			List<Integer> subList3=new ArrayList<Integer>();
			List<Integer> subList4=new ArrayList<Integer>();
			
			subList1.addAll(foodSource.subList(0,indexList.get(0)));
			subList2.addAll(foodSource.subList(indexList.get(0),indexList.get(1)));
			subList3.addAll(foodSource.subList(indexList.get(1),indexList.get(2)+1));//target swap subsequence
			Collections.reverse(subList3);
			subList4.addAll(foodSource.subList(indexList.get(2)+1,foodSource.size()));
			
			foodSource.clear();
			foodSource.addAll(subList1);
			foodSource.addAll(subList3);
			foodSource.addAll(subList2);
			foodSource.addAll(subList4);
		}		
		return foodSource;	
	}
	
	public List<Integer> combined()
	{
		List<Integer> foodSource = new ArrayList<Integer>();
		int rand=0;
		rand=randomInt(0,3);
		switch(rand)
		{
			case 0:
				foodSource.addAll(this.swapReverseSubsequence());
				break;
			case 1:
				foodSource.addAll(this.reverseSubsequence());
				break;
			case 2:
				foodSource.addAll(this.swap());
				break;
			default:
				break;
		}
		return foodSource;
	}
	*/
	public static List<Integer> insertQueenNode(List<Integer> foodSource)//insert depot node into the sequence
	{
		List<Integer> depotNodes = new ArrayList<Integer>();
		//find the position of the depot node in the list
		for(int i=0;i<foodSource.size();i++)
		{
			if(foodSource.get(i)==1)
				depotNodes.add(i);
		}
		
		int[] target=new int[2];
		int maxDemand=0;
		int demand=0;
		//find a route with the largest demand
		maxDemand=calDemand(depotNodes.get(0),depotNodes.get(1),foodSource);
		target[0]=depotNodes.get(0);target[1]=depotNodes.get(1);
		
		for(int i=1;i<depotNodes.size()-1;i++)
		{
			demand=calDemand(depotNodes.get(i),depotNodes.get(i+1),foodSource);
			if(demand>maxDemand)
			{
				maxDemand=demand;
				target[0]=depotNodes.get(i);target[1]=depotNodes.get(i+1);
			}
		}
		int index=randomInt(target[0]+1,target[1]);
		// adding a new depot node within the route with the largest customer demand
		foodSource.add(index, 1);
		return foodSource;
	}
	
	public static List<Integer> removeQueenNode(List<Integer> foodSource)//remove depot node from the sequence
	{
		List<Integer> depotNodes = new ArrayList<Integer>();
		//find the position of the depot node in the list
		for(int i=0;i<foodSource.size();i++)
		{
			if(foodSource.get(i)==1)
				depotNodes.add(i);
		}
		
		//will fail to remove nodes if depot node number is smaller than 3
		if(depotNodes.size()<=3)
		{
			System.out.println("failure");
			return foodSource;
		}

		
		int target;
		int minDemand=0;
		int demand=0;
		//find a route with the largest demand
		minDemand=calDemand(depotNodes.get(0),depotNodes.get(1),foodSource)+calDemand(depotNodes.get(1),depotNodes.get(2),foodSource);
		target=depotNodes.get(1);
		
		for(int i=2;i<depotNodes.size()-1;i++)
		{
			demand=calDemand(depotNodes.get(i-1),depotNodes.get(i),foodSource)+calDemand(depotNodes.get(i),depotNodes.get(i+1),foodSource);
			if(demand<minDemand)
			{
				minDemand=demand;
				target=depotNodes.get(i);
			}
		}
		//remove the depot node between adjacent routes with the least overall demand
		foodSource.remove(target);	
		return foodSource;
	}
	
	public static int calDemand(int head, int tail, List<Integer> foodSource)//[head+1,tail-1]
	{
		int sum=0;
		for(int i=head+1;i<tail;i++)
		{
			sum+=CVRPData.getDemand(foodSource.get(i));
		}
		return sum;
	}
	
	public static double calCost(List<Integer> foodSource)
	{
		double sum=0.0;
		for(int i=0;i<foodSource.size()-1;i++)
		{
			sum+=CVRPData.getDistance(foodSource.get(i), foodSource.get(i+1));
		}
		return sum;
	}
	
	public static double calViolateCapacity(List<Integer> foodSource)
	{
		double sum=0.0;
		List<Integer> depotNodes = new ArrayList<Integer>();
		//find the position of the depot node in the list
		for(int i=0;i<foodSource.size();i++)
		{
			if(foodSource.get(i)==1)
				depotNodes.add(i);
		}
		
		int demand=0;
		
		for(int i=0;i<depotNodes.size()-1;i++)
		{		
			demand=calDemand(depotNodes.get(i),depotNodes.get(i+1),foodSource);
			if(demand>CVRPData.VEHICLE_CAPACITY)
				sum+=(demand-CVRPData.VEHICLE_CAPACITY);
		}
		return sum;
	}
	
	public static double calFitness(double cost, double violateCapacity)
	{
		double fitness=0.0;
		//fitness function
		fitness=1+6000/(cost+Configuration.ALPHA*violateCapacity);
		return fitness;
		
	}
	//number of vehicle used equals to total number of depot nodes-1
	public static int countVehicleNum(List<Integer> foodSource)
	{
		int length = foodSource.size();
		int sum=0;
		if(length==0)
			return 0;
		else
		{
			sum=countDepotNode(foodSource, 0, length )-1;
		}
		return sum;
	}
	
	//[start end)
	private static int countDepotNode(List<Integer> foodSource, int start, int end)
	{
		int sum=0;
		if(start==end)
			return 0;
		else if(start+1==end)
		{
			if(foodSource.get(start)==1)
				return 1;
			else
				return 0;
		}
		else
		{
			if(foodSource.get(start)==1)
				sum++;
			if(foodSource.get(end-1)==1)
				sum++;
			sum+=countDepotNode(foodSource, start+1,end-1);
		}
		return sum;
	}
	
	public static int sum(List<Integer> ints)
	{
	    int len = ints.size();
	    if (len == 0) return 0;
	    if (len == 1) return ints.get(0);
	    return sum(ints.subList(0, len/2)) + sum(ints.subList(len/2, len));
	}
	
	public void setFoodSource(List<Integer> foodSource)
	{
		this.foodSource.clear();
		this.neighborFoodSource.clear();
		this.foodSource.addAll(foodSource);
		this.iteration=0;
		this.cost=Food.calCost(foodSource);
		this.violateCapacity=Food.calViolateCapacity(foodSource);
		this.fitness=Food.calFitness(cost, violateCapacity);
		this.vehicleNum=Food.countVehicleNum(foodSource);
	}

	public void copy(Food copySource)
	{
		
	}
	
	public static void copy(Food copySource, Food CopyDirection)
	{
		
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		Food foodSource = (Food) super.clone();

		return foodSource;
	}	
	
	public static void main(String[] args) throws IOException
	{
		Food food=new Food();
		int v =Food.randomInt(0,2);
		System.out.println(v);

		CVRPData.readFile("fruitybun250.vrp");
		food.initializeFood(CVRPData.NUM_NODES);
		Print.printList(food.foodSource);
		
		System.out.println(food.cost);
		System.out.println(food.violateCapacity);
		System.out.println(food.fitness);
		
		Print.print(food.foodSource.contains(1));
		Print.printList(food.foodSource.subList(0, 1));//[0,5)
		food.foodSource.addAll(1, food.foodSource.subList(0, 5));//insert from 1 e.i 
		Print.printList(food.foodSource);
		
		List<Integer> L= new ArrayList<Integer>();
		L.addAll(food.foodSource.subList(1, 6));
		food.foodSource.removeAll(L);
		Print.printList(food.foodSource);
		food.foodSource.clear();
		food.foodSource.addAll(L);
		Print.printList(food.foodSource);
		Collections.sort(food.foodSource);
		Print.printList(food.foodSource);
		//Graph graph = new Graph();
		List<Integer> list=new ArrayList<Integer>();	
		
		food.initializeFood(20);
		Print.printList(food.foodSource);
		
		list=Operator.insert(food.foodSource);
		//list=food.insert();
		Print.printList(list);
		
		list=Operator.insertSubsequence(food.foodSource);
		//list=food.insertSubsequence();
		Print.printList(list);
		
		list=Operator.insertReverseSubsequence(food.foodSource);
		//list=food.insertReverseSubsequence();
		Print.printList(list);
		
		list=Operator.swap(food.foodSource);		
		//list=food.swap();
		Print.printList(list);
		
		list=Operator.swapSubsequence(food.foodSource);	
		//list=food.swapSubsequence();
		Print.printList(list);
		
		list=Operator.swapReverseSubsequence(food.foodSource);	
		//list=food.swapReverseSubsequence();
		Print.printList(list);
		
		list=Operator.reverseSubsequence(food.foodSource);	
		//list=food.reverseSubsequence();
		Print.printList(list);
		
		list=Operator.combined(food.foodSource);	
		//list=food.combined();
		Print.printList(list);
	}
}
