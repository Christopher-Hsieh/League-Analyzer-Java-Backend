package com.meta.analyzer.service;

import java.util.ArrayList;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meta.analyzer.jest.AggregateSummonerChampionsAndItems;
import com.meta.analyzer.jest.dto.ExtractedChampionItemCountDto;
import com.meta.analyzer.jest.dto.ExtractedItemTotalsDto;
import com.meta.analyzer.riot.api.aggregator.SimpleItemAggregator;

@Service
public class RequestProcessor implements Runnable {

	@Autowired
	Queue<String> incomingSummonerQueue;
	
	@Autowired
	SimpleItemAggregator simpleItemAggregator;
    
	@Autowired
	AggregateSummonerChampionsAndItems aggregateSummonerChampionsAndItems;
	
	public void run(){
		System.out.println("Spawning Request Processor");
		Thread t = Thread.currentThread();
		System.out.println("Name: " + t.getName() + " Priority: " + t.getPriority() + " String: " + t.toString() + " ActiveCount: " + t.activeCount() + " ID: "+ t.getId());
		
		while (true) {
			if (!incomingSummonerQueue.isEmpty()) {
				String summonerName = incomingSummonerQueue.remove();
				
		    	// Make sure match history is as up to date as possible
		    	simpleItemAggregator.pullAndStoreSummonerData(summonerName);
		    	
		    	// Aggregate their matches!
		    	ArrayList<ExtractedChampionItemCountDto> championItemCountList = aggregateSummonerChampionsAndItems.extractChampionsAndItems(summonerName);
		    	ExtractedChampionItemCountDto championItemCount = championItemCountList.get(0);
		    	championItemCount.getChampionId();
		    	championItemCount.getGamesPlayedAsChampion();
		    	ArrayList<ExtractedItemTotalsDto> itemTotalsList = championItemCount.getItemTotalsList();
		    	itemTotalsList.get(0).getItemCount();
		    	itemTotalsList.get(0).getItemId();
			}
		}
	
	}
}