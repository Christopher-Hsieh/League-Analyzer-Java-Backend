package com.meta.analyzer.riot.api.aggregator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amazonaws.http.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meta.analyzer.ApplicationProperties;
import com.meta.analyzer.jest.AggregateSummonerChampionsAndItems;
import com.meta.analyzer.jest.PutMatchData;
import com.meta.analyzer.riot.api.grabber.GetMatchHistory;
import com.meta.analyzer.riot.api.grabber.GetMatchItems;
import com.meta.analyzer.riot.dto.MatchDataDto;
import com.meta.analyzer.riot.dto.RetrievedItemListDto;
import com.meta.analyzer.service.RateManager;

import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

// Given summoner Name
// Get Item %'s for all champions
/*
 * Get an entire summoners match history worth of items
 */
@Component
@Scope("prototype")
public class SimpleItemAggregator {


	@Autowired
	PutMatchData putMatchData;
	
	@Autowired
	ApplicationProperties applicationProperties;
	
    @Autowired
    private WebApplicationContext context;
    
    static Logger logger = Logger.getLogger(SimpleItemAggregator.class.getName());
    
    RateManager rateManager;
	GetMatchItems matchItems;
	GetMatchHistory matchHistory;
	
	@PostConstruct
    public void init() {
    	this.rateManager = (RateManager) context.getBean("rateManager");
    	this.matchHistory = (GetMatchHistory) context.getBean("getMatchHistory");
    	this.matchItems = (GetMatchItems) context.getBean("getMatchItems");
    }
    
		
	public void pullAndStoreSummonerData(String summonerName) {
		/*
		 * Pull Match History
		 */
		Summoner summoner = rateManager.getSummonerByName(summonerName);
		Map<Long, Collection<Long>> championMatchMap = matchHistory.getMatchHistory(summoner);

		if (championMatchMap.isEmpty()) {
			logger.info("No Champions found in match History!");
			return;
		}

		/*
		 * Pull Items.
		 * Store Match.
		 */
		if (applicationProperties.getGetOnlyMostPlayedChampion() == true) {
			storeMostPlayedChampion(championMatchMap, summoner);
		}
		else {
			storeEntireMatchHistory(championMatchMap, summoner);
		}

	}
	
	public void storeEntireMatchHistory(Map<Long, Collection<Long>> championMatchMap, Summoner summoner) {
		int currentMatch = 0;
		int totalMatches = matchHistory.getTotalMatches();
		
		for (long championID : championMatchMap.keySet()) {
			ArrayList<Long> matchIdList = (ArrayList<Long>) championMatchMap.get(championID);
			for (long matchID : matchIdList) {
				currentMatch++;
				logger.info("Match " + currentMatch + "/" + totalMatches);
				RetrievedItemListDto itemListData = matchItems.getMatchItemsForSummoner(matchID, summoner.getAccountId());

				if (itemListData != null) {
					
					storeMatchData(
							setMatchData(summoner, championID, matchID, itemListData)
							);
				}
			}

		}
	}
	
	public void storeMostPlayedChampion(Map<Long, Collection<Long>> championMatchMap, Summoner summoner) {
		// Find Most Played Champ
		long mostPlayedChampionID = -1;
		int largestMatchList = 0;
		for (long championID : championMatchMap.keySet()) {
			if (largestMatchList < championMatchMap.get(championID).size()) {
				largestMatchList = championMatchMap.get(championID).size();
				mostPlayedChampionID = championID;
			}
		}
		
		
			ArrayList<Long> matchIdList = (ArrayList<Long>) championMatchMap.get(mostPlayedChampionID);
			int currentMatch = 0;
			int totalMatches =  matchIdList.size();
			
			logger.info("Only Getting Most Played Champion");
			
			for (long matchID : matchIdList) {
				currentMatch++;
				logger.info("Match " + currentMatch + "/" + totalMatches);
				RetrievedItemListDto itemListData = matchItems.getMatchItemsForSummoner(matchID, summoner.getAccountId());

				if (itemListData != null) {
					
					storeMatchData(
							setMatchData(summoner, mostPlayedChampionID, matchID, itemListData)
							);
				}
			}
	}
	
	/*
	 * TODO: remove. we can replace with new MatchDataDto(summoner, championID, matchID, itemListData)
	 */
	public MatchDataDto setMatchData(Summoner summoner, long championID, long matchID, RetrievedItemListDto itemListData) {
		MatchDataDto matchData = new MatchDataDto();
		matchData.setSummonerName(summoner.getName());
		matchData.setSummonerID(summoner.getId());
		matchData.setAccountID(summoner.getAccountId());
		matchData.setChampionID(championID);
		matchData.setMatchID(matchID);
		matchData.setItemList(itemListData);
		//matchData.printMatchData();
		return matchData;
	}
	
	public void storeMatchData(MatchDataDto matchData) {
			int response = putMatchData.put(matchData);
			logger.info("Response Code from ES: " + response);
	}
}

