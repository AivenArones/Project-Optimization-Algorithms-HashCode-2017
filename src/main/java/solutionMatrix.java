package main.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class solutionMatrix {

	public String submissionFileStr;
	private int[][] solutionMatrix;
	private int score;
	public boolean validSolution;
	Map<String, Object> data;

	// --------------------Initialize solution Matrix-----------------------------//
	public solutionMatrix(Map<String, Object> data) {
		
		this.data=data;
		int numberOfCaches = (int) data.get("number_of_caches"); // Rows
		int numberOfFiles = (int) data.get("number_of_videos"); // Coloumns

		// Create blank solutionMatrix

		boolean cacheConstraintValid = true;
		int solutionMatrix2[][] = new int[numberOfCaches][numberOfFiles];

		for (int i = 0; i < numberOfCaches; i++) {

			for (int i2 = 0; i2 < numberOfFiles; i2++) {
				solutionMatrix2[i][i2] = 0;
			}
			
			
		}

		solutionMatrix = solutionMatrix2;
		validSolution = constraintTest(data);
		score = 0;
		submissionFileStr = submissionsFileFormatEncoder(data, solutionMatrix);
	}

	// ---------- Submissions File format Encoder--------------//

	/*
	 Purpose: Will translate a 2-D solutionMatrix into a parseable format, the
	 format used is the one outlined in the given
	 "Hashcode_2017_qualification_round.pdf", I refer to this format as Submission
	 File Format
	 
	 Submission File Format  

	solutionStr	|				Meaning
		 3  	| We are using all 3 cache servers. 
		0 2 	| Cache server 0 contains only video 2. 			
	  1 3 1 	| Cache server 1 contains videos 3 and 1. 
	  2 0 1 	| Cache server 2 contains videos 0 and 1.
	 
	 */

	public String submissionsFileFormatEncoder(Map<String, Object> data, int[][] solutionMatrix) {

		int numberOfCachesUsed = 0;
		int numberOfFiles = (Integer) data.get("number_of_videos");
		int numberOfCaches = (Integer) data.get("number_of_caches");

		ArrayList<Integer> cachesUsed = new ArrayList<Integer>();

		StringBuilder sb = new StringBuilder(); // Create StringBuilder

//------Find number of caches used, corresponds to the first line of the output String------//
		for (int i8 = 0; i8 < numberOfCaches; i8++) {

			for (int i9 = 0; i9 < numberOfFiles; i9++) {

				if (solutionMatrix[i8][i9] == 1) {

					numberOfCachesUsed = numberOfCachesUsed + 1;
					cachesUsed.add(i8);
					i9 = numberOfFiles + 10; // end current forLoop

				}

			}

		}

		sb.append(numberOfCachesUsed); // Number of Caches used
		sb.append("\n"); // Next Line

		for (int i3 = 0; i3 < numberOfCachesUsed; i3++) {

			sb.append(i3);// Add Cache

			for (int i4 = 0; i4 < numberOfFiles; i4++) {

				// If there is a 1, there is a file in this cache

				if (solutionMatrix[i3][i4] == 1) {
					sb.append(" "); // Add Space to output String
					sb.append(i4); // Add file to output String
				}

			}

			if ((i3 + 1) < numberOfCachesUsed == true) {
				sb.append("\n"); // NextLine
			}
		}

		return String.valueOf(sb);
	}

//------------------------- Support Methods ---------------------------//

	// -------- Solution Matrix (Getter & Setter) ---------//

	public int[][] getSolutionMatrix() {
		int[][]solutionMatrix = this.solutionMatrix;
		return solutionMatrix;
	}

	

	public void setSolutionMatrix(Map<String, Object> data, int[][] newSolution) {

		solutionMatrix = newSolution;
		validSolution = constraintTest(data);

		
		score=calculateScore(data);
		submissionFileStr = submissionsFileFormatEncoder(data, solutionMatrix);
	}
	
	public int getScore() {
		
		return calculateScore(data);
	}

	// ---------- Constraint Test -----------------//
	public boolean constraintTest(Map<String, Object> data) {

		safetyConstraintTest test = new safetyConstraintTest();
		validSolution = test.satisfyConstraintTest(data, solutionMatrix);
		
		return validSolution;

	}

	// ------------ Print --------------//
	public void print(Map<String, Object> data) {

		System.out.println(Arrays.deepToString(solutionMatrix));
		//System.out.println("Valid Solution = " + constraintTest(data));
		System.out.println("Output: \n");
		System.out.println(submissionFileStr);
		System.out.println("\nScore: "+getScore());
	}
	
	

	   public int calculateScore(Map<String, Object> data) {
	       
	    
	    	
	    
			ArrayList<file> fileList = new ArrayList<file>(); //ArrayList of Files
			String solutionStr = submissionFileStr; //Translate solutionMatrix into Parseable "Submission File Format"

	/*
		Note: Submission File Format (Found in "Hashcode_2017_qualification_round.pdf")
		
		solutionStr     |  			 Meaning
			3          	| We are using all 3 cache servers.
			0 2		  	| Cache server 0 contains only video 2.
			1 3 1	  	| Cache server 1 contains videos 3 and 1.
			2 0 1	  	| Cache server 2 contains videos 0 and 1.	
				
	*/
			LinkedList<String> linesList = new LinkedList<>(Arrays.asList(solutionStr.split("\\R"))); 
			linesList.remove(); //Remove first element, it is unneeded because it tells us how many caches are used (unnecessary information)

	//--------------------Part 1: Add Files to fileList-----------------------
			for(int i=0;i<(Integer)data.get("number_of_videos");i++) {		
				
				
				file newFile=new file(data,i);
				fileList.add(newFile);
			
			}
			
			//-------------Part 2: Fill in information in each file------------------//		
					for(int i2=0;i2<fileList.size();i2++) {
						
						int fileNumber=fileList.get(i2).number;
						
						//Parse through solution String (what file is in each cache)
						for(int i3=0;i3<linesList.size();i3++) {
							
							//currLine will tell us what files are in a cache, e.g "0 2" translates to File 2 is in Cache 0
							String currLine=linesList.get(i3); 
							
							//Turn String currLine into LinkedList to use "pop" instructions, makes parsing faster and allows for ".contains" to be used
							LinkedList<String>line=new LinkedList<>(Arrays.asList(currLine.split(" ")));
							
							int currCache=Integer.parseInt(line.pop());
							//System.out.println(currCache);
							
							if(line.contains(String.valueOf(fileNumber))) {
								fileList.get(i2).containedInCaches.add(currCache);
							}
						}
						
						
					
					//	fileList.get(i2).print();
						//System.out.println();
					}
			
		
			calculateScore cs= new calculateScore(data, fileList,solutionMatrix);
			//System.out.println(cs.score);
			
			return (int)cs.score;
	    }
	
			 
			 	
	
	
	}


