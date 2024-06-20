package main.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

//Random Restart Hill Climbing: (Different Algorithms)
//Abstract Procedure:

/*
 	Random Restart Hill-Climbing, also known as "Shotgun Hill Climbing"
 
 		1. Create initial solution, in this case it is a random valid solution
 		2. fittestSolution = initialSolution
 		3. Generate randomised solution 
		4. Compare the score of the randomised solution with fittestSolution
		5. If it is worse go back to step 3, if it is equal or better go to step 6
		6. Since we have found a better score, fittestSolution = randomised solution found in step 3
		7. Go back to step 3
		
	Comments:
		
		This algorithm is an evolution of simple hill climbing, I implemented this to coincide with the project's need to go "beyond", initially it 
		was a result of my attempt to improve my initial hill climbing solution. Through further research, I learned that my idea to improve the 
		initial hill climbing solution was a pre-existing algorithm called "Random Restart Hill-Climbing" (Shotgun Hill Climbing).
		
		"Considered to be a meta-algorithm which is built on top of the hill climbing algorithm, 
		random-restart hill climbing or shotgun hill climbing performs the process iteratively 
		with a random initial condition, in each phase." 
															- https://www.professional-ai.com/hill-climbing-algorithm.html
		
		There is a special termination condition in generateRandomState(), there is variable called "numberOfIteration", it counts the number of times
		a new random solution was generated. If after 10 or any number of iterations, it will end the loop and add the current fittestSolution to the
		list of neighbours. This is because at this point, no discernable progress has been made. Ideally I would have the cut off point be higher
		but for it would take too long.
		
		In each case, the list of neighbours should always be in ascending order, so the tail of the list should be the fittestSolution by only adding
		a solution to the list of neighbours if it is better or equal to the current fittestSolution. I do not have to iterate through the list to find
		the current best solution. Because I am constantly updating it.
		
		31/03/2024:
		I have decided to use Random Restart Hill-Climbing to initialize a random population for the geneticAlgorithm, this design is both a greedy/random
		approach and uses Hill-Climbing principles. An initial population of random solutions makes for a more diverse population,

				"The diversity of the population should be maintained otherwise it might lead to premature convergence."
																										-https://www.tutorialspoint.com/genetic_algorithms/genetic_algorithms_population.htm

  
 */

public class randomRestartHillclimbing {
	
	Map<String, Object> data;
	ArrayList<file> files;
	LinkedList<solutionMatrix> neighbours;
	
	solutionMatrix fittestSolution;
	int bestScore;
	int numberOfNeighbours=100;
	
	
	public randomRestartHillclimbing(Map<String, Object> data,boolean forGeneticAlgorithm) throws IOException {


		this.data=data;
		neighbours=new LinkedList<solutionMatrix>();
		if(forGeneticAlgorithm==false){
		//Add initial state 
		solutionMatrix sc=new solutionMatrix(data);
		sc.setSolutionMatrix(data, generateRandomState());
		neighbours.add(sc);
		
		fittestSolution=sc;
		fittestSolution.calculateScore(data);
		
		bestScore=fittestSolution.getScore();
		generateNeighbours();
		//System.out.println(bestScore);
		//System.out.println(neighbours.size());
		}
	}

	public int[][]fittestSolution() throws IOException{
		

		fittestSolution.calculateScore(data);
		return fittestSolution.getSolutionMatrix();
	}
	
    
    
    
    
	public void generateNeighbours() throws IOException {
		while(neighbours.size()!=numberOfNeighbours) {
			int[][]randomState=generateRandomState();
			
			solutionMatrix sc= new solutionMatrix(data);
			sc.setSolutionMatrix(data, randomState);
			sc.calculateScore(data);
			neighbours.add(sc);
			//System.out.println(neighbours.size());
		}
	}

	
	public int[][] generateRandomState() throws IOException {
		int numberOfFiles = (int) data.get("number_of_videos");
		int numberOfCaches = (int) data.get("number_of_caches");
		solutionMatrix sc2 = new solutionMatrix(data);
		int fileRandIndexRange = (numberOfFiles - 1) - 0 + 1;
		int cacheRandIndexRange = (numberOfCaches - 1) - 0 + 1;
		boolean validSolution = false;
		// new int[numberOfCaches][numberOfFiles];
		int numberOfIterations=0;
		while (validSolution == false) {
			

			// Create random Number between 2 and 10
			int max = numberOfFiles*2;
			int min = 0;
			int range = max - min + 1;

			int randomNumberOfMoves = (int) (Math.random() * range) + min;
			
			if(validSolution==false){
			// put file in a random Cache
				solutionMatrix sc = new solutionMatrix(data);
			for (int i = 0; i < randomNumberOfMoves; i++) {
			
				int randomFileIndex = (int) (Math.random() * fileRandIndexRange) + 0;
				int randomCacheIndex = (int) (Math.random() * cacheRandIndexRange) + 0;

				int[][]temp=sc.getSolutionMatrix();
				
				temp[randomCacheIndex][randomFileIndex] = 1;
				sc.setSolutionMatrix(data, temp);
			}
			
			safetyConstraintTest st=new safetyConstraintTest();

			if (st.satisfyConstraintTest(data, sc.getSolutionMatrix()) == true&&sc.getScore()>=bestScore&&sc.getScore()!=0) {

				//System.out.println(ri.solutionMatrixIntoString(sc.solutionMatrix));
				//System.out.println("Is Valid");
			//	System.out.println("Score = " + sc.getScore());
				
				validSolution=true;
				sc.calculateScore(data);
				bestScore=sc.getScore();
				fittestSolution=sc;
				sc2.setSolutionMatrix(data, sc.getSolutionMatrix());
				//neighbours.poll();
			} else {

				//System.out.println(ri.solutionMatrixIntoString(sc.solutionMatrix));
				//System.out.println("Is not Valid");
				//System.out.println("Score = " + sc.score);
				
				numberOfIterations=numberOfIterations+1;
			}
			
			if(numberOfIterations>1000) {
			//An inferior solution has been found 100 time, no discernable progress made, end while loop
				validSolution=true; 
				sc2=fittestSolution;
			}
			
		}
			// System.out.println(ri.solutionMatrixIntoString(sc2.solutionMatrix));
		}
		
		sc2.getScore();
		
		return sc2.getSolutionMatrix(); // Placeholder
	}

	//Initialize Random Population for genetic algorithm

	public LinkedList<int[][]> initializeGeneticPopulation(Map<String, Object> data,int populationSize){
		LinkedList<int[][]> geneticPopulation2=new LinkedList<int[][]>();
		int numberOfFiles = (int) data.get("number_of_videos");
		int numberOfCaches = (int) data.get("number_of_caches");


		for(int i3=0;i3<populationSize;i3++){

		solutionMatrix sc2 = new solutionMatrix(data);
		int fileRandIndexRange = (numberOfFiles - 1) - 0 + 1;
		int cacheRandIndexRange = (numberOfCaches - 1) - 0 + 1;
		boolean validSolution = false;
		
		while (validSolution == false) {
			
			int max = numberOfFiles*2;
			int min = 1;
			int range = max - min + 1;

			int randomNumberOfMoves = (int) (Math.random() * range) + min;
			
			if(validSolution==false){
			// put file in a random Cache
				solutionMatrix sc = new solutionMatrix(data);
			for (int i = 0; i < randomNumberOfMoves; i++) {
			
				int randomFileIndex = (int) (Math.random() * fileRandIndexRange) + 0;
				int randomCacheIndex = (int) (Math.random() * cacheRandIndexRange) + 0;

				int[][]temp=sc.getSolutionMatrix();
				
				temp[randomCacheIndex][randomFileIndex] = 1;
				sc.setSolutionMatrix(data, temp);
			}
			
			safetyConstraintTest st=new safetyConstraintTest();

			if (st.satisfyConstraintTest(data, sc.getSolutionMatrix()) == true) {

				validSolution=true;
				sc.calculateScore(data);
				bestScore=sc.getScore();
				fittestSolution=sc;
				sc2.setSolutionMatrix(data, sc.getSolutionMatrix());

				geneticPopulation2.add(sc2.getSolutionMatrix());
			} 
	
			
		}
		}
	}

	return geneticPopulation2;
	}
	
	public void printNeighbours() {
		
		int index=0;
		for(solutionMatrix neighbour: neighbours) {
			System.out.println("//-------------------------- Solution: "+(index+1)+"------------------------------//\n");
			System.out.println("2D-Array Representation: \n"+Arrays.deepToString(neighbour.getSolutionMatrix()));
			System.out.println("\nSubmission File Representation: \n"+neighbour.submissionFileStr);
			System.out.println("Score "+neighbour.getScore());
			System.out.println();
			index=index+1;
		}
	}

}
