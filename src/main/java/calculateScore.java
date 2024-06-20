package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class calculateScore {

	/*
	 * Pseudocode (from project brief):
	 
	  Scoring function
	 
	  	=“cost function” 
		=“utility function” 
		=“fitness function”
	  
	 score = 0
	 
		 for each request q for a file f at an endpoint e
	 
	 		score += number of videos in the request * (latency from data centre – best latency from a cache server serving e and hosting f)
	  
	  endfor
	 
	 score = score/number of requests 
	 score *= 1000
	 
	 
	 */

	public double score;

	public calculateScore(Map<String, Object> data, ArrayList<file> files, int[][] solutionMatrix) {

		double score = 0;
		int totalNumberOfRequests = 0;

		// Part 1:
		for (file file : files) {

			totalNumberOfRequests = totalNumberOfRequests + file.numberOfRequests;
			endpoint endpoint = new endpoint(data, file.endpoint);
			double bestCacheLatency = endpoint.latencyToDataCentre; // Initialize with data center latency
			
			if (endpoint.cacheLatency.containsKey(-1) != true) {
				// Find the best cache latency for this file serving endPoint e and hosting file f
				for (int i = 0; i < file.containedInCaches.size(); i++) {
					//System.out.println(endpoint.cacheLatency);
					double newCacheLatency = endpoint.cacheLatency.get(file.containedInCaches.get(i));

					if (newCacheLatency < bestCacheLatency) {
						bestCacheLatency = newCacheLatency;
					}

				}
			}
			
			// score += number of videos in the request * (latency from data centre – best latency from a cache server serving e and hosting f)
			score += file.numberOfRequests * (endpoint.latencyToDataCentre - bestCacheLatency);
			
			//System.out.println("File: " + file.number);
			//System.out.println(	file.numberOfRequests + " * (" + (endpoint.latencyToDataCentre - bestCacheLatency) + ")");
			//System.out.println(	file.numberOfRequests + " * (" + endpoint.latencyToDataCentre + " - " + bestCacheLatency + ")");
		}

		// Part 2:

		score = score / totalNumberOfRequests; // score = score/number of requests

		// Convert average score to microseconds and return

		score = score * 1000; // score *= 1000
		this.score = score;
	}
}
