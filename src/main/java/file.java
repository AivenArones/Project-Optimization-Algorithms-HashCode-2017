package main.java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//
/*

 Comments: When observing the behaviour of the "video_ed_request" HashMap, I found that the key contained information regarding the file and endpoint. The
 key could be split into two values (the file and the endpoint). The value of the HashMap also corresponded to the number of requests.
 All of this was necessary information when creating the fitness function. 
 
 	//Example: Kittens.in
	//Key = 3794,360 (fileNumber = 3794, endpoint = 360 )
		
	//Example: example.in
	//Key = 0,1 (fileNumber = 0, endpoint = 1)
	//Value = numberOfRequests = 1000
 */

public class file {
	
	public int number; //file number
	public int numberOfRequests; //numberOfRequests
	public int endpoint; //endpoint (if one exists)
	public int size; //size
	public int latencyToDataCentre; //Either 0 or endpoint latency
	public List<Integer> containedInCaches;
	public file(Map<String, Object> data,int fileNumber) {
		
		latencyToDataCentre=0;
		containedInCaches= new LinkedList<Integer>();
		
		number=fileNumber;
		Map<String, String> video_ed_request = (Map<String, String>) data.get("video_ed_request");
	
		numberOfRequests=0;
		endpoint= -1;
		
		for (Map.Entry<String, String> entry : video_ed_request.entrySet()) {	
		
			

			String key = entry.getKey();
			
			//keyArr[0] = fileNumber, keyArr[1]=endpoint
			String[]keyArr=key.split(","); 
			
		
			
			int tempFileNumber=Integer.parseInt(keyArr[0]);
			
			
			if(tempFileNumber==fileNumber) {
							
				try {
					
					/*
					Used to observe function behaviour:
					
					System.out.println("Key = "+entry.getKey());
					System.out.println("Value = "+entry.getValue()+"\n");				
					System.out.println("Video = "+keyArr[0]);
					System.out.println("Endpoint = "+keyArr[1]);
					*/
					
					
					endpoint=Integer.parseInt(keyArr[1]); //
					numberOfRequests =Integer.parseInt(entry.getValue());
					
					
				}catch (Exception e) {
					
				}

				break; //file found
			}
			
	
		}
		
		
		if(endpoint!=-1) {
			endpoint e=new endpoint(data, endpoint);
			latencyToDataCentre=e.latencyToDataCentre;
		}
		int[]video_size_desc=(int[])data.get("video_size_desc");	
		size=video_size_desc[number]; //Get file size
		
	}
	
	
	public void print() {
		
		System.out.println("File Number = "+number);
		System.out.println("Size = "+size);
		System.out.println("Number of Requests = "+numberOfRequests);
		System.out.println("Endpoint = "+endpoint);
		System.out.println("Latency to Data Centre = "+latencyToDataCentre);
		System.out.println("Caches = "+containedInCaches);
	}
}
