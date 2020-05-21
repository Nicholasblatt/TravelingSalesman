// Name: Nicholas Blatt, NetId: bf4698
// created genetic algorithm to solve traveling salesman problem

package mypack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

	
	// here is my fitness function that calculates the cost of a given path
	// this uses a 2d array filled with elements from the teacher's given chart
	public int fitFunc(int[] path) {
		int cost = 0;
		int costs[][] = { 
				{ 0, 2, 11, 3, 18, 14, 20, 12, 5 }, 
				{ 2, 0, 13, 10, 5, 3, 8, 20, 17 },
				{ 11, 13, 0, 5, 19, 21, 2, 5, 8 }, 
				{ 3, 10, 5, 0, 6, 4, 12, 15, 1 }, 
				{ 18, 5, 19, 6, 0, 12, 6, 9, 7 },
				{ 14, 3, 21, 4, 12, 0, 19, 7, 4 }, 
				{ 20, 8, 2, 12, 6, 19, 0, 21, 13 },
				{ 12, 20, 5, 15, 9, 7, 21, 0, 6 }, 
				{ 5, 17, 8, 1, 7, 4, 13, 6, 0 } 
			};
		for (int i = 0; i < path.length - 1; i++) {
			cost += costs[path[i]][path[i+1]];
		}
		
		return cost;
	}
	// this is how i randomly produce states to start off the genetic algorithm
	public int [] genState() 
	{
		Integer returnArr[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		List<Integer> stateToMake = Arrays.asList(returnArr);
		Collections.shuffle(stateToMake);
		stateToMake.toArray(returnArr);
		int normalIntArr [] = {returnArr[0], returnArr[1], returnArr[2], 
				returnArr[3], returnArr[4], returnArr[5], 
				returnArr[6], returnArr[7], returnArr[8], returnArr[0]}; 
		
		return normalIntArr;
	}
	// the tournment select I used to find the fittest node
	// teacher said it was OK even though its not in the book
	public int [] tournSelect(int pop[][])
	{
		Random rand = new Random();
		int randNum = rand.nextInt(990);
		int fittest = fitFunc(pop[randNum]);
		int challenger;
		int stateToReturn [] = new int [10];
		stateToReturn = pop[randNum].clone();
		for(int i = randNum + 1; i < randNum + 10; i++)
		{
			challenger = fitFunc(pop[i]);
			if(challenger < fittest)
			{
				fittest = challenger;
				stateToReturn = pop[i].clone();
			}
		}
		return stateToReturn.clone();
	}
	// my crossover functions helper
	public void findAndSwap(int geno, int current, int child[])
	{
		int temp;
		for(int i = 0; i < child.length; i++)
		{
			if(geno == child[i])
			{
				temp = child[current];
				child[current] = child[i];
				child[i] = temp;
				child[9] = child[0];
				return;
			}
		}
		
	}
	// this is my crossover function
	public int [] reproduce(int mom[], int dad[])
	{
		Random rand = new Random();
		int [] child = new int [10];
		int n = 9;
		int c = rand.nextInt(n);
		child = mom.clone();
		for(int i = 0; i < c; i++)
		{
			findAndSwap(dad[i], i, child);
		}
		child[9] = child[0];
		return child;
	}
	
	// this is my core genetic algorithm with a population of 1000
	// I used tournement select to find the fittest nodes in this case
	public int [] geneticAlg()
	{
		int qq = 0;
		int chall;
		int fittest = 1000;
		int [] mom = new int [10];
		int [] dad = new int [10];
		int [] child = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 0};
		int [] best = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 0};
		int[][] myPop = new int[1000][10];
		int[][] newPop = new int[1000][10];
		// generating the original population with random states
		for(int i = 0; i < 1000; i++)
		{
			myPop[i] = genState();
		}
		// finding initial best in population
		for(int i = 0; i < 1000; i++)
		{
			chall = fitFunc(myPop[i]);
			if(chall < fittest)
			{
				fittest = chall;
				best = myPop[i].clone();
			}
		}
		// qq is to prevent infinite loops as a timer
		// this is in case the solution is not found in a reasonable time
		// stops when a fitness value of 35 is reached for the best one
		// I could not get any lower than 35 even when lowing this number
		while(fitFunc(best) > 35 && qq < 10000)
		{
			// new population generator
			for(int i = 0; i < 1000; i++)
			{

				mom = tournSelect(myPop);
				dad = tournSelect(myPop);
				while(dad == mom && qq < 10000)
				{
					dad = tournSelect(myPop);
					qq++;
				}
				child = reproduce(mom, dad);
				// mutation here
				findAndSwap(child[i%9], 5, child);
				// mutation will occur most times on a random piece
				newPop[i] = child;
			}
			// making our new generation our starting point for next generation
			myPop = newPop;
			// finding the fittest of the new population to check if we are done
			for(int i = 0; i < 1000; i++)
			{
				chall = fitFunc(myPop[i]);
				if(chall < fittest)
				{
					fittest = chall;
					best = myPop[i];
				}
			}
			qq++;
		}
		return best;
	}
	

	public Main() {
		// need a random state generator for population
		int a[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 0};
		a = geneticAlg();
		// path found by genetic alg
		System.out.print("ran genetic alg and found best path\nthe best path to take is from\ncity ");
		for(int i = 0; i < 10; i++)
		{
			if(i < 9)
			{
				System.out.print( (a[i] + 1) + " to ");
			}
			else
			{
				System.out.print("arrive back at city " + (a[i] + 1));
			}
		}
		// fitness value of final node
		System.out.print("\nwith total miles traveled being: " + fitFunc(a));
	}

	public static void main(String[] args) {
		new Main();

	}

}
