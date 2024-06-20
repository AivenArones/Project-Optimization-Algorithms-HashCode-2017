package main.java;

import java.io.*;
import java.util.*;



public class ReadInput {
    public Map<String, Object> data;

    public ReadInput() {
        data = new HashMap<String, Object>();
    }

    public int fitness(int[][] solutionMatrix2) {
       
    	solutionMatrix solutionMatrix = new solutionMatrix(data);
    	
    	solutionMatrix.setSolutionMatrix(data, solutionMatrix2);
    	
    	boolean safetyConstraintTest = solutionMatrix.constraintTest(data);

		if (safetyConstraintTest == false) {
			return -1;
		}
		
		ArrayList<file> fileList = new ArrayList<file>(); //ArrayList of Files
		String solutionStr = solutionMatrix.submissionFileStr; //Translate solutionMatrix into Parseable "Submission File Format"

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
		
	
		calculateScore cs= new calculateScore(data, fileList,solutionMatrix2);
		//System.out.println(cs.score);
		
		return (int)cs.score;
    }

    public void readGoogle(String filename) throws IOException {
             
        BufferedReader fin = new BufferedReader(new FileReader(filename));
    
        String system_desc = fin.readLine();
        String[] system_desc_arr = system_desc.split(" ");
        int number_of_videos = Integer.parseInt(system_desc_arr[0]);
        int number_of_endpoints = Integer.parseInt(system_desc_arr[1]);
        int number_of_requests = Integer.parseInt(system_desc_arr[2]);
        int number_of_caches = Integer.parseInt(system_desc_arr[3]);
        int cache_size = Integer.parseInt(system_desc_arr[4]);
    
        Map<String, String> video_ed_request = new HashMap<String, String>();
        String video_size_desc_str = fin.readLine();
        String[] video_size_desc_arr = video_size_desc_str.split(" ");
        int[] video_size_desc = new int[video_size_desc_arr.length];
        for (int i = 0; i < video_size_desc_arr.length; i++) {
            video_size_desc[i] = Integer.parseInt(video_size_desc_arr[i]);
        }
    
        List<List<Integer>> ed_cache_list = new ArrayList<List<Integer>>();
        List<Integer> ep_to_dc_latency = new ArrayList<Integer>();
        List<List<Integer>> ep_to_cache_latency = new ArrayList<List<Integer>>();
        for (int i = 0; i < number_of_endpoints; i++) {
            ep_to_dc_latency.add(0);
            ep_to_cache_latency.add(new ArrayList<Integer>());
    
            String[] endpoint_desc_arr = fin.readLine().split(" ");
            int dc_latency = Integer.parseInt(endpoint_desc_arr[0]);
            int number_of_cache_i = Integer.parseInt(endpoint_desc_arr[1]);
            ep_to_dc_latency.set(i, dc_latency);
    
            for (int j = 0; j < number_of_caches; j++) {
                ep_to_cache_latency.get(i).add(ep_to_dc_latency.get(i)); //Removed "+1", provided incorrect information
            }
    
            List<Integer> cache_list = new ArrayList<Integer>();
            for (int j = 0; j < number_of_cache_i; j++) {
                String[] cache_desc_arr = fin.readLine().split(" ");
                int cache_id = Integer.parseInt(cache_desc_arr[0]);
                int latency = Integer.parseInt(cache_desc_arr[1]);
                cache_list.add(cache_id);
                ep_to_cache_latency.get(i).set(cache_id, latency);
            }
            ed_cache_list.add(cache_list);
        }
    
        for (int i = 0; i < number_of_requests; i++) {
            String[] request_desc_arr = fin.readLine().split(" ");
            
           
            String video_id = request_desc_arr[0];
            String ed_id = request_desc_arr[1];
            String requests = request_desc_arr[2];
            video_ed_request.put(video_id + "," + ed_id, requests);
        }
    
        data.put("number_of_videos", number_of_videos);
        data.put("number_of_endpoints", number_of_endpoints);
        data.put("number_of_requests", number_of_requests);
        data.put("number_of_caches", number_of_caches);
        data.put("cache_size", cache_size);
        data.put("video_size_desc", video_size_desc);
        data.put("ep_to_dc_latency", ep_to_dc_latency);
        data.put("ep_to_cache_latency", ep_to_cache_latency);
        data.put("ed_cache_list", ed_cache_list);
        data.put("video_ed_request", video_ed_request);
    
        fin.close();
     
     }
    
    public boolean temp=false;

     public String toString() {
        String result = "";

        //for each endpoint: 
        for(int i = 0; i < (Integer) data.get("number_of_endpoints"); i++) {
            result += "enpoint number " + i + "\n";
            //latendcy to DC
            int latency_dc = ((List<Integer>) data.get("ep_to_dc_latency")).get(i);
            result += "latency to dc " + latency_dc + "\n";
            //for each cache
            for(int j = 0; j < ((List<List<Integer>>) data.get("ep_to_cache_latency")).get(i).size(); j++) {
                int latency_c = ((List<List<Integer>>) data.get("ep_to_cache_latency")).get(i).get(j); 
                result += "latency to cache number " + j + " = " + latency_c + "\n";
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException { 
    	
    	//Found mistake: Incorrect Output in example.in file

        ReadInput ri = new ReadInput();
        ri.readGoogle("input/example.in");
        
//------------------- First Choice hillClimbing -----------------------//
        hillClimbing h= new hillClimbing(ri.data);

        h.printNeighbours();

        System.out.println("Hill-climbing fittest solution:  ");
       // h.fittestSolution.print(ri.data);

//-------- Random Restart hill-climbing (Shotgun hill-climbing) ----------//
        randomRestartHillclimbing r = new randomRestartHillclimbing(ri.data, false);
       // r.printNeighbours();
       System.out.println("Random Restart hill-climbing fittest solution:  ");
       r.fittestSolution.print(ri.data);

//----------------------- Genetic Algorithm --------------------//
        geneticAlgorithm g = new geneticAlgorithm(ri.data);

        g.print();

        System.out.println("Genetic Algorithm fittest solution:  ");
        g.fittestSolution.print(ri.data);

    }
}
