package main.java;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/*
	Abstract:
	Hill-climbing algorithms work in two steps:
	
		• generate all the neighbouring solutions, by making simple “moves”
		• keep the best (fittest) solution.
		
		1. Define the initial state (initial solution), fittest solution = initial solution
		2. Simple move: add random file to cache or remove random file from cache 
		3. Check if solution is valid (cache capacity constraint), if not go back to step 2 
		4. Compare the new state with the fittest solution (scoring), if equal or better add to neighbours and update fittest solution
		5. Go back to Step 2
	

	
	Comments:
		
		According to Geek for Geeks, Hill-Climbing algorithms have the below strengths and weaknesses

		Advantages of Hill Climbing algorithm:
			1. Hill Climbing is a simple and intuitive algorithm that is easy to understand and implement.
			2. It can be used in a wide variety of optimization problems, including those with a large search space and complex constraints.
			3. Hill Climbing is often very efficient in finding local optima, making it a good choice for problems where a good solution is needed quickly.
			4. The algorithm can be easily modified and extended to include additional heuristics or constraints.

		Disadvantages of Hill Climbing algorithm:
			1. Hill Climbing can get stuck in local optima, meaning that it may not find the global optimum of the problem.
			2. The algorithm is sensitive to the choice of initial solution, and a poor initial solution may result in a poor final solution.

												Source: https://www.geeksforgeeks.org/introduction-hill-climbing-artificial-intelligence/

		Considerations:

			I made use of advantage 4 and extended the initial instructions to include additional constraints (cache capacity constraint), I also took into account
			disadvantage 2 by creating a randomized initial solution. I did this because I originally used a blank solution with a score of 0, this made a large
			portion of the neighbours to be awful solutions at the start. Using a random initial solution created better solutions. 

			In order to create more diverse solutions, the simple move takes a greedy/random approach. This makes my design a First Choice Hill-Climbing algorithm.
			This was not intentional but instead
		
 */


public class hillClimbing {

	boolean blank;
	Random r;
	int numberOfFiles;
	int numberOfCaches;
	int numberOfNeighbours = 50;
	solutionMatrix fittestSolution;

	LinkedList<solutionMatrix> neighbours;

	public hillClimbing(Map<String, Object> data) {
		blank =true;
		r = new Random();
		numberOfFiles = (int) data.get("number_of_videos");
		numberOfCaches = (int) data.get("number_of_caches");
		//neighboursString = new LinkedList<String>();
		neighbours = new LinkedList<solutionMatrix>();
		
		// Initialize base case

		solutionMatrix initialState = new solutionMatrix(data);
		fittestSolution = initialState;
		neighbours.add(initialState);
		
		
		while(neighbours.size()<numberOfNeighbours) {

			generateNeighbours(data);
		
		}

	}

	public void generateNeighbours(Map<String, Object> data) {

		int fileRandIndexRange =r.nextInt(numberOfFiles-0) + 0;
		int cacheRandIndexRange =r.nextInt(numberOfCaches-0) + 0;

		
		
		
		int[][]tempSolution = fittestSolution.getSolutionMatrix();
		
		//Make Simple Move
		if(tempSolution[cacheRandIndexRange][fileRandIndexRange]==0) {
			tempSolution[cacheRandIndexRange][fileRandIndexRange]=1;
		}else {
			tempSolution[cacheRandIndexRange][fileRandIndexRange]=0;
		}
		
		
		safetyConstraintTest s= new safetyConstraintTest();

		if(s.satisfyConstraintTest(data, tempSolution)==true) {
				
			solutionMatrix sm=new solutionMatrix(data);
			
			sm.setSolutionMatrix(data, tempSolution);
			
			
			if(sm.getScore()>=fittestSolution.getScore()) {

				fittestSolution=sm;
				neighbours.add(fittestSolution);
				
				
			}
			
			
		
		}
		
		
	}
	
	
	//Print neighbours
	
	public void printNeighbours() {
		
		int index=0;
		for(solutionMatrix neighbour: neighbours) {
			System.out.println("//-------------------------- Solution: "+index+"------------------------------//\n");
			System.out.println("2D-Array Representation: \n"+Arrays.deepToString(neighbour.getSolutionMatrix()));
			System.out.println("\nSubmission File Representation: \n"+neighbour.submissionFileStr);
			System.out.println("Score "+neighbour.getScore());
			System.out.println();
			index=index+1;
		}
	}

	
	
	
	
}
