package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class endpoint {

   public int endpointNumber;

   public int latencyToDataCentre;
	
   public  HashMap<Integer, Integer> cacheLatency;
   public  List<Integer> cacheList;
   
	public endpoint(Map<String, Object> data,int number) {
		
		endpointNumber=number;
		cacheLatency = new HashMap<Integer,Integer>();
		
		//endpointNumber -1 = no endpoint, in data centre
		if(endpointNumber!=-1) {
		
		//Get latency to data centre
		latencyToDataCentre = ((List<Integer>) data.get("ep_to_dc_latency")).get(endpointNumber); 
		
		//Create List of Caches that are conneceted to endpoint
		List<List<Integer>> temp=(List<List<Integer>>) data.get("ed_cache_list");  
		cacheList = temp.get(endpointNumber); 
		
		
		cacheLatency = new HashMap<Integer,Integer>();
		temp = ((List<List<Integer>>) data.get("ep_to_cache_latency"));
		List<Integer> endpointToCacheLatencyList =temp.get(endpointNumber);
		
		
		for(int i=0;i<endpointToCacheLatencyList.size();i++) {
			cacheLatency.put(i, endpointToCacheLatencyList.get(i));
		}
		
		}else {
			cacheLatency.put(-1, 0);
		}
	
	}
	
	
	public void printEndpointDetails() {
		System.out.println("Endpoint number: "+endpointNumber);
		
		System.out.println("Latency to Data Centre: "+latencyToDataCentre);
		System.out.println("List of Caches: "+cacheList);
		
		System.out.println(cacheLatency.toString());
	}
}
