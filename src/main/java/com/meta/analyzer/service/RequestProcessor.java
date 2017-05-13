package com.meta.analyzer.service;

import java.util.Queue;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meta.analyzer.riot.api.aggregator.SimpleItemAggregator;

@Component
public class RequestProcessor implements Runnable{
	@Resource
	Queue<String> incomingSummonerQueue;
	
	@Autowired
	SimpleItemAggregator simpleItemAggregator;
	
	public void run(){
		System.out.println("Spawning Request Processor");
		while (true) {
			System.out.println("Thread running");
			if (!incomingSummonerQueue.isEmpty()) {
				String summonerName = incomingSummonerQueue.remove();
				System.out.println(summonerName + " removed from queue. Queue size now: " + incomingSummonerQueue.size());
		    	// Make sure match history is as up to date as possible
		    	simpleItemAggregator.pullAndStoreSummonerData(summonerName);
			}
		}
	
	}
}