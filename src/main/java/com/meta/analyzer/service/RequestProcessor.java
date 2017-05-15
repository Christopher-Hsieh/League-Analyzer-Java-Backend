package com.meta.analyzer.service;

import java.util.Queue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.meta.analyzer.riot.api.aggregator.SimpleItemAggregator;

@Component
public class RequestProcessor implements Runnable{
	@Resource
	Queue<String> incomingSummonerQueue;
	
    @Autowired
    private WebApplicationContext context;
	
    static Logger logger = Logger.getLogger(RequestProcessor.class.getName());
    
    public SimpleItemAggregator getSimpleItemAggregator() {
    	return (SimpleItemAggregator) context.getBean("simpleItemAggregator");
    }
	
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
				    	 getSimpleItemAggregator().pullAndStoreSummonerData(summonerName);
//				     }
//				}).start();
			}
		}
	
	}
}