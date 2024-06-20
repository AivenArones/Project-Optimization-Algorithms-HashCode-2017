package main.java;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
public class geneticAlgorithm {

	//Currently in progress of making, 31/03/2024
	
	
	/*
	Genetic Algorithm:

		 	
			Genotype Representation: Binary Representation
			Mutation: Bit-flip mutation (number of bit flips varies to maintain diversity)
			Crossover: One Point crossover
			Population Initialization: Random Initialization (Greedy/Random), populates the initial population with completely random solutions using Random Restart hill-climbing to generate population

	Comments:

			According to the brief, I had two options when initializing the population. I could either use the hill climbing principles or a greedy/random algorithm. After contemplation I decided
			to use my Random Restart Hill-Climbing algorithm since it was a greedy/random algorithm and made use of the hill climbing principles. However, I made a slight adjustment in my design, 
			to avoid duplicates I only add a gene to the population if it does not already exist. Using binary representation made finding duplicates easy as ArrayLists have in-built ways to find 
			if an element already exists. This greedy, random approach creates a diverse population and using my pre-existing hillclimbing design sped up development.
	


	Definitions:
	Population − It is a subset of all the possible (encoded) solutions to the given problem. The population for a GA is analogous to the population for human beings except that instead of human beings,
				 we have Candidate Solutions representing human beings.
	Chromosomes − A chromosome is one such solution to the given problem.
	Gene − A gene is one element position of a chromosome.
	Allele − It is the value a gene takes for a particular chromosome.

	-https://www.tutorialspoint.com/genetic_algorithms/genetic_algorithms_mutation.htm


							
	 */

	 Map<String, Object> data;
	 //Initialize Population
	solutionMatrix fittestSolution;

	 int initalizedPopulationSize=50;
	int numberOfFailures;
	 ArrayList <String> population;
	 
	 public geneticAlgorithm(Map<String, Object> data2)throws IOException{

		data=data2;

		//initialize population (Greed/Random Approach)
		population = initializePopulation(data); 

		int numberOfGenerations =5;

		int numberOfIterations=0;

		

		
		Double probabilityOfMutation =0.01;
		while(numberOfIterations<numberOfGenerations){


			
			while(population.size()<100){
					
				//Cross over or mutation
				//Create random Number between 1 and 100 ()
				int max = 100;
				int min = 1;
				int range = max - min + 1;
		
				int randomNumber = (int) (Math.random() * range) + min;

				if(randomNumber!=5){
					//crossover
					crossover();
				}else{

					String[]parents=parentSelection();
					bitFlipMutation(data2, parents[0]);
				}
			}

			sortPopulation(); //Sort population (by fitness)

			while(population.size()>50){
				population.remove(0); //Remove least fit solutions
			}

			numberOfIterations=numberOfIterations+1;
		}

		int[][]fittestSolution1=binaryDecoder(population.get(population.size()-1));
		solutionMatrix s= new solutionMatrix(data);
		s.setSolutionMatrix(data2, fittestSolution1);
		fittestSolution=s;
	 }

	 
	 //One-Point Crossover
	 public void crossover(){
		
		boolean bothChildrenValid=false;
		while(bothChildrenValid==false){

		//Select parents
		String[]parents=parentSelection();
		
		//Create Random Parent Partions

		String[]parent1 = new String[2];
		String[]parent2 = new String[2];

		Random r=new Random();

		// Create random Number between 3 and solution string length
		int max = parents[0].length();
		int min = 3;
		int range = max - min + 1;
		
		int randomPoint = (int) (Math.random() * range) + min;
		
		//Partition parent 1
		parent1[0] = parents[0].substring(0, randomPoint);
		parent1[1] = parents[0].substring(randomPoint);

		//Partition parent 2
		parent2[0] = parents[1].substring(0, randomPoint);
		parent2[1] = parents[1].substring(randomPoint);

		
		
		//Create child 1
		StringBuilder sb = new StringBuilder();
		sb.append(parent1[0]);
		sb.append(parent2[1]);

		String child1=sb.toString();

		//Create child 2
		StringBuilder sb2 = new StringBuilder();
		sb2.append(parent1[1]);
		sb2.append(parent2[0]);

		String child2=sb2.toString();


		//Note: the below if condition ensures no invalid solution is added and maintains diversity by not adding duplicates

		safetyConstraintTest s=new safetyConstraintTest();
		if(s.satisfyConstraintTest(data, binaryDecoder(child1))==true&&s.satisfyConstraintTest(data, binaryDecoder(child2))==true&&population.contains(child1)!=true&&population.contains(child2)!=true){
			//System.out.println("valid");

			//System.out.println("Parents: "+sb.toString()+", "+sb2.toString());
			//System.out.println("Children: "+ child1+", "+child2);

			population.add(child1);
			population.add(child2);

			bothChildrenValid=true;
		}
		
		}
		

	 }
	 //Parent Selection: Ranking Selection
	 public String[]parentSelection(){
	
		//Sort population
		sortPopulation();

		//Since population is already sorted, they are ranked accordingly: ArrayList Index = rank

		Random r = new Random();
		/* 

		Assign selection probabilities:

		s = selection pressure parameter, N = population Size
		p(i) = (2 - s) / N + 2  (i - 1)  (s - 1) / (N * (N - 1)), formula found in  Ref: https://www.linkedin.com/pulse/selections-genetic-algorithms-ali-karazmoodeh-g9yyf/

		*/



		//HashMap<String,Double>solutionToSelectionProb= new HashMap<String,Double>();
		LinkedList<Double>selectionProbabilities= new LinkedList<Double>();

		Double s=1.6;
		int N = population.size();

		for(int i=0;i<population.size();i++){

		//i = rank
		double selectionProbability =(2 - s) / N + 2  * (i - 1)  *(s - 1) / (N * (N - 1));

		selectionProbabilities.add(selectionProbability);
		}

		
		

		//Select Parent 1;

		String[]parents=new String[2];
		double randomProbability = r.nextDouble(); //random probability between 0 and 1
        double cumulativeProbability = 0;

		boolean validParent= false;
		//System.out.println("prob 1: "+randomProbability);

		while(validParent==false){
        for (int i = 0; i < selectionProbabilities.size(); i++) {
            cumulativeProbability =cumulativeProbability+selectionProbabilities.get(i);
            if (randomProbability <= cumulativeProbability) {
                String parent1=population.get(i);
				parents[0]=parent1;
				i=selectionProbabilities.size()+1;
            }

			if(parents[0]!=null){
				validParent=true;
			}
        }

	}

		//Select Parent 2
		Random r2= new Random();
		double randomProbability2 = r2.nextDouble();
		cumulativeProbability = 0;

		//System.out.println("prob 2: "+randomProbability2);

		validParent =false;

		while(validParent==false){
		for (int i2 = 0; i2 < selectionProbabilities.size(); i2++) {
            cumulativeProbability =cumulativeProbability+selectionProbabilities.get(i2);
            if (randomProbability2 <= cumulativeProbability) {
                String parent2=population.get(i2);
				parents[1]=parent2;
				i2=selectionProbabilities.size()+1;
            }

        }

		if(parents[1]!=null&&parents[0]!=parents[1]){
			validParent=true;
		}
	}


		//System.out.println(Arrays.deepToString(parents));
		return parents;


	}
	


	public void sortPopulation(){

		LinkedList<String>sortedPopulation=new LinkedList<>();
		HashMap <String,Integer>solutionToScore=new HashMap<String,Integer>();

		for(int i2=0;i2<population.size();i2++){
			solutionToScore.put(population.get(i2), fitnessForEncoded(population.get(i2)));
		}
		population=new ArrayList<>();


	//The below methodolgy of creating a list that sorts a hashmap by values was found from GeekForGeeks, Ref: https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/

	// Create a list from elements of HashMap
	List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(solutionToScore.entrySet());

		  
   // Sort the list
   Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
	   public int compare(Map.Entry<String, Integer> o1, 
						  Map.Entry<String, Integer> o2)
	   {
		   return (o1.getValue()).compareTo(o2.getValue());
	   }
   });
	
  

for(int i=0;i<list.size();i++){
	sortedPopulation.add(list.get(i).getKey());
}



	population=new ArrayList<String>();

	while(sortedPopulation.isEmpty()!=true){
		population.add(sortedPopulation.removeFirst());
	}

   }


	

	 //Method to print population in 2D-Array Representation, Binary Representation and submissionFileFormat
	 public void print(){
		for(int i=0;i<population.size();i++){
			int[][]solutionMatrix=binaryDecoder(population.get(i));
			String binaryRepresentation=population.get(i);
			int score=fitness(solutionMatrix);

			solutionMatrix temp=new solutionMatrix(data);
			temp.setSolutionMatrix(data, solutionMatrix);

			System.out.println("\nSolution "+(i+1)+":");
			System.out.println("\n"+Arrays.deepToString(solutionMatrix));
			
			System.out.println(binaryRepresentation);
			System.out.println("\n"+temp.submissionFileStr);
			System.out.println("Score: "+score);
		}
	 }

	 //Initialize population using a greedy/random approach and hill-climbing principles
	 public ArrayList<String> initializePopulation(Map<String, Object> data) throws IOException{
		
		randomRestartHillclimbing rh=new randomRestartHillclimbing(data, true);
		ArrayList<String>geneticPopulation=new ArrayList<String>();

		while(geneticPopulation.size()<initalizedPopulationSize){
			//create chromosome
			int[][]chromosome=rh.generateRandomState();
			
			//encode chromosome
			String encodedChromosome=binaryEncoder(chromosome);

			//No duplicates constraint
			if(geneticPopulation.contains(encodedChromosome)==false){

			//Add to geneticPopulation
			geneticPopulation.add(binaryEncoder( chromosome));

			}
			
			
		}


return geneticPopulation;
	 }
	 public String crossover(Map<String, Object> data,String binarySolution,String binarySolution2){
return "a";

	 }
	//Mutation 1: Bit Flip Mutation (One or Several bit flips)
	
	public String bitFlipMutation(Map<String, Object> data,String binarySolution) {
		
		Random rand = new Random();
		int max;
		int numberOfBitFlips =1;
		int tempInt=rand.nextInt(4 - 1 + 1) + 1;
	
		//Occasional increase of the number of bit flips for increase diversity and less chance of plateua
		if(tempInt>3) {			
			int min = 1;
			max = binarySolution.length()-1;		

			//Choose a random number of bit flips
			numberOfBitFlips =  rand.nextInt(max - min + 1) + min;		
			System.out.println("\nSeveral Bit Flips");	
		}

		StringBuilder tempStr= new StringBuilder();
		tempStr.append(binarySolution);
		
		
		max=binarySolution.length()-1;
		
		for(int i=0;i<numberOfBitFlips;i++) {
			
			//Choose random bit in String
			int bitIndex=   rand.nextInt(max - 0 + 1) + 0;
			
			//Bit flip
			if(tempStr.charAt(bitIndex)=='0') {

				tempStr.setCharAt(bitIndex, '1');
				
			}else {
				tempStr.setCharAt(bitIndex, '0');				
			}
		}
		//System.out.println(tempStr.toString());
		return tempStr.toString();
	}
	
	//2D-Array to binary String
	public String binaryEncoder(int[][]solution) {
		StringBuilder binaryRepresentation = new StringBuilder();
		
		int numberOfFiles = (int) data.get("number_of_videos");
		int numberOfCaches = (int) data.get("number_of_caches");
		
		for(int i=0;i<numberOfCaches;i++) {
			
			for(int i2=0;i2<numberOfFiles;i2++) {
				
				binaryRepresentation.append(solution[i][i2]);
				
			}
		}
		
		return binaryRepresentation.toString();
	}
	
	//Binary string to 2D-Array
	public int[][]binaryDecoder(String encodedSolution){
		int numberOfFiles = (int) data.get("number_of_videos");
		int numberOfCaches = (int) data.get("number_of_caches");
		
		int[][]solution=new int[numberOfCaches][numberOfFiles];
		
		int stringIndex =0;
		for(int i=0;i<numberOfCaches;i++) {
			
			for(int i2=0;i2<numberOfFiles;i2++) {
				
				solution[i][i2]=Integer.parseInt(String.valueOf(encodedSolution.charAt(stringIndex)));
				stringIndex=stringIndex+1;
			}
		}
		
		return solution;
		
	}


	//Fitness function
	public int fitness(int[][]solution){

		solutionMatrix tempSolutionMatrix = new solutionMatrix(data);
		tempSolutionMatrix.setSolutionMatrix(data, solution);
		tempSolutionMatrix.calculateScore(data);
		return tempSolutionMatrix.getScore();
		
	}

		//Fitness for encoded binary representation
		public int fitnessForEncoded(String solution){

			solutionMatrix tempSolutionMatrix = new solutionMatrix(data);
			tempSolutionMatrix.setSolutionMatrix(data, binaryDecoder(solution));
			tempSolutionMatrix.calculateScore(data);
			return tempSolutionMatrix.getScore();
			
		}
}
