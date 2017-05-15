package com.meta.analyzer.service;

import java.util.Queue;

import javax.annotation.Resource;

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
	
    public SimpleItemAggregator getSimpleItemAggregator() {
    	return (SimpleItemAggregator) context.getBean("simpleItemAggregator");
    }
	
    @Async
	public void run(){
		System.out.println("Spawning Request Processor");

		while (true) {
			System.out.println("true");
			if (!incomingSummonerQueue.isEmpty()) {
				String summonerName = incomingSummonerQueue.remove();
				System.out.println(summonerName + " removed from queue. Queue size now: " + incomingSummonerQueue.size());
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