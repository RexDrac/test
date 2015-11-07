package CVRP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Operator
{
	private static Random random = new Random(System.currentTimeMillis());

	public static int randomInt(int min,int max)
	{
		int rand=0;
		rand=min+random.nextInt(max-min);//[min,max)
		//System.out.println(random.nextBoolean());
		return rand;
	}	
	
	public static List<Integer> swap(List<Integer> inputFoodSource)
	{
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(inputFoodSource);
		
		while(true)
		{
			int i1=0,i2=0,element1=0,element2=0;
			i1=Operator.randomInt(1,foodSource.size()-1);//first and last elements are start nodes, exclude
			do
			{
				i2=Operator.randomInt(1,foodSource.size()-1);				
			}
			while(i1==i2);//keep finding until i1!=i2
			
			//System.out.println(i1);
			//System.out.println(i2);
			//do not allow depot node to be swapped
			if(foodSource.get(i1)==1||foodSource.get(i2)==1)//if any of the selected node are depot nodes, find a new swap set
				continue;	
			element1=foodSource.get(i1);
			element2=foodSource.get(i2);
			//allow start node to be swapped
			/*
			if(foodSource.get(i1)==1&&foodSource.get(i2)==1)//if both selected node are depot nodes, find a new swap set
				continue;
			if((element1=foodSource.get(i1))==1&&(element2=foodSource.get(i2))!=1)
			{
				if((foodSource.get(i2-1)==1)||(foodSource.get(i2+1)==1))// second node is adjacent to a depot node, find another set
					continue;
			}	
			if((element1=foodSource.get(i1))!=1&&(element2=foodSource.get(i2))==1)
			{
				if((foodSource.get(i1-1)==1)||(foodSource.get(i1+1)==1))// first node is adjacent to a depot node, find another set
					continue;
			}
			*/
			foodSource.set(i1,element2);
			foodSource.set(i2,element1);		
			break;
		}
		return foodSource;
	}
	
	public static List<Integer> swapSubsequence(List<Integer> inputFoodSource)
	{
		List<Integer> invalidIndex = new ArrayList<Integer>();//index that should not be chosen
		List<Integer> indexList = new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource=new ArrayList<Integer>();
		foodSource.addAll(inputFoodSource);//Copy the value into local variables
		
		do//find four diverse index that contains depot nodes
		{
			index=Operator.randomInt(1,foodSource.size()-1);
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
	
	public static List<Integer> insert(List<Integer> inputFoodSource)
	{
		List<Integer> indexList=new ArrayList<Integer>();
		List<Integer> invalidIndex=new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(inputFoodSource);
		
		do
		{
			index=Operator.randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node foodn taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			
			}
			
		}while(indexList.size()<2);
		Collections.sort(indexList);
		if(random.nextBoolean()/*randomInt(0,2)==0*/)//randomly generate 0 or 1
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
	
	public static List<Integer> insertSubsequence(List<Integer> inputFoodSource)
	{
		List<Integer> indexList=new ArrayList<Integer>();
		List<Integer> invalidIndex=new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(inputFoodSource);
		
		do
		{
			index=Operator.randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node foodn taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			
			}
			
		}while(indexList.size()<3);
		Collections.sort(indexList);
		if(random.nextBoolean()/*randomInt(0,2)==0*/)//randomly generate 0 or 1
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
	
	public static List<Integer> reverseSubsequence(List<Integer> inputFoodSource)
	{
		List<Integer> indexList=new ArrayList<Integer>();
		List<Integer> invalidIndex=new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(inputFoodSource);
		
		do
		{
			index=Operator.randomInt(1,foodSource.size()-1);
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
	
	public static List<Integer> swapReverseSubsequence(List<Integer> inputFoodSource)
	{
		List<Integer> invalidIndex = new ArrayList<Integer>();//index that should not be chosen
		List<Integer> indexList = new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(inputFoodSource);
		
		do//find four diverse 
		{
			index=Operator.randomInt(1,foodSource.size()-1);
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
	
	public static List<Integer> insertReverseSubsequence(List<Integer> inputFoodSource)
	{
		List<Integer> indexList=new ArrayList<Integer>();
		List<Integer> invalidIndex=new ArrayList<Integer>();
		int index=0;
		
		List<Integer> foodSource = new ArrayList<Integer>();
		foodSource.addAll(inputFoodSource);
		
		do
		{
			index=Operator.randomInt(1,foodSource.size()-1);
			if(!invalidIndex.contains(index)&&foodSource.get(index)!=1)//
			{
				indexList.add(index);
				invalidIndex.add(index-1);
				invalidIndex.add(index);// node foodn taken, should not be taken any more
				invalidIndex.add(index+1);//adjacent nodes should also be excluded, add adjacent nodes to the invalid list
			
			}
			
		}while(indexList.size()<3);
		Collections.sort(indexList);
		if(random.nextBoolean()/*randomInt(0,2)==0*/)//randomly generate 0 or 1
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
	
	public static List<Integer> combined(List<Integer> inputFoodSource)
	{
		List<Integer> foodSource = new ArrayList<Integer>();
		int rand=0;
		rand=Operator.randomInt(0,3);
		switch(rand)
		{
			case 0:
				foodSource.addAll(Operator.swapReverseSubsequence(inputFoodSource));
				break;
			case 1:
				foodSource.addAll(Operator.reverseSubsequence(inputFoodSource));
				break;
			case 2:
				foodSource.addAll(Operator.swap(inputFoodSource));
				break;
			default:
				break;
		}
		return foodSource;
	}
}
