package main.java;

import java.util.Map;

public class safetyConstraintTest {

	
	//---------------- Test Constraint ---------------------//
	//Ensures in each cache that the sum of all file sizes associated with said cache is less than
	//the maximum capacity of the cache
	//Formula: fileSizeSum < cacheSize

		public boolean satisfyConstraintTest(Map<String, Object> data,int[][] solutionMatrix) {

			boolean cacheConstraintValid = true;

			int numberOfFiles = (Integer) data.get("number_of_videos");
			int numberOfCaches = (Integer) data.get("number_of_caches");
			int cacheSize = (Integer) data.get("cache_size");

		//----------------------Validate Solution Matrix-------------------------//

			int[] videoSizeArr = (int[]) data.get("video_size_desc");

			//i3 = curr cache
			for (int i3 = 0; i3 < numberOfCaches; i3++) {

				int fileSizeSum = 0; //size of all files in cache i3

				//i4 = curr file
				for (int i4 = 0; i4 < numberOfFiles; i4++) {

					if (solutionMatrix[i3][i4] == 1) {
						
						fileSizeSum = fileSizeSum + videoSizeArr[i4]; //Add size of file
						
					}
					
				}

				if (fileSizeSum > cacheSize) {
					cacheConstraintValid = false;
					//System.out.println("Cache size exceeded in Cache "+i3);
				}
			}

			if (cacheConstraintValid != true) {
				//System.out.println("solution not valid");
				return false;
			}

			if (cacheConstraintValid == true) {
				//System.out.println("The solution is valid");
				return true;
			}
			
			return cacheConstraintValid;
		}

}
