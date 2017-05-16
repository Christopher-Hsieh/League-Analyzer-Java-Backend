package com.meta.analyzer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.meta.analyzer.riot.api.aggregator.MatchesAggregator;

@Component
public class RequestProcessor implements Runnable{
	@Resource
	Queue<String> incomingSummonerQueue;
	
	@Autowired
	MatchesAggregator matchesAggregator;
	
	
    static Logger logger = Logger.getLogger(RequestProcessor.class.getName());
    

    
    @Async
	public void run(){
		logger.info("Spawning Request Processor");

		while (true) {
			logger.info("Processor Running");
			if (!incomingSummonerQueue.isEmpty()) {
				String summonerName = incomingSummonerQueue.remove();
				logger.info(summonerName + " removed from queue. Queue size now: " + incomingSummonerQueue.size());
		    	// Make sure match history is as up to date as possible
//				new Thread(new Runnable() {
//				     public void run() {
				    	 matchesAggregator.pullAndStoreSummonerData(summonerName);
//				     }
//				}).start();
			}
		}
	
	}
}