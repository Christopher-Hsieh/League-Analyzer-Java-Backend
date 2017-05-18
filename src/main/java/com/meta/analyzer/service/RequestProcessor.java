package com.meta.analyzer.service;

import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.meta.analyzer.riot.api.aggregator.MatchesAggregator;

@Component
public class RequestProcessor implements Runnable{
	@Resource
	BlockingQueue<String> incomingSummonerQueue;
	
	@Autowired
	MatchesAggregator matchesAggregator;
	
	
    static Logger logger = Logger.getLogger(RequestProcessor.class.getName());


    private String TryTakeSummonerName() {
    	String summonerName = null;
		try {
			summonerName = incomingSummonerQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return summonerName;
	}
    
    @Async
	public void run(){
		logger.info("Spawning Request Processor");

		while (true) {
			logger.info("Processor Running");
			String summonerName = TryTakeSummonerName();
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