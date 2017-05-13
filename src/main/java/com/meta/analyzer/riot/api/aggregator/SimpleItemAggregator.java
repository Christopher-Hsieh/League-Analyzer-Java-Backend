package com.meta.analyzer.riot.api.aggregator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.http.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meta.analyzer.ApplicationProperties;
import com.meta.analyzer.rest.aws.SimpleESPost;
import com.meta.analyzer.riot.api.grabber.GetMatchHistory;
import com.meta.analyzer.riot.api.grabber.GetMatchItems;
import com.meta.analyzer.riot.dto.MatchDataDto;
import com.meta.analyzer.riot.dto.RetrievedItemListDto;

// Given summoner Name
// Get Item %'s for all champions
/*
 * Get an entire summoners match history worth of items
 */
@Component
public class SimpleItemAggregator {

	@Autowired
	GetMatchHistory matchHistory;
	
	@Autowired
	GetMatchItems matchItems;

	@Autowired
	SimpleESPost simpleESPost;
	
	@Autowired
	ApplicationProperties applicationProperties;
		
	public void pullAndStoreSummonerData(String summonerName) {
		
		/*
		 * Pull Match History
		 */
		Map<Long, Collection<Long>> championMatchMap = matchHistory.getMatchHistory(summonerName);

		if (championMatchMap.isEmpty()) {
			System.out.println("No Champions found in match History!");
			return;
		}

		/*
		 * Pull Items.
		 * Store Match.
		 */
		if (applicationProperties.getGetOnlyMostPlayedChampion() == true) {
			storeMostPlayedChampion(championMatchMap, summonerName);
		}
		else {
			storeEntireMatchHistory(championMatchMap, summonerName);
		}

	}
	
	public void storeEntireMatchHistory(Map<Long, Collection<Long>> championMatchMap, String summonerName) {
		int currentMatch = 0;
		int totalMatches = matchHistory.getTotalMatches();
		
		for (long championID : championMatchMap.keySet()) {
			ArrayList<Long> matchIdList = (ArrayList<Long>) championMatchMap.get(championID);
			for (long matchID : matchIdList) {
				currentMatch++;
				System.out.println("Match " + currentMatch + "/" + totalMatches);
				RetrievedItemListDto itemListData = matchItems.getMatchItemsForSummoner(matchID, matchHistory.getAccountID());

				if (itemListData != null) {
					
					storeMatchData(
							setMatchData(summonerName, championID, matchID, itemListData)
							);
				}
			}

		}
	}
	
	public void storeMostPlayedChampion(Map<Long, Collection<Long>> championMatchMap, String summonerName) {
		// Find Most Played Champ
		long mostPlayedChampionID = -1;
		int largestMatchList = 0;
		for (long championID : championMatchMap.keySet()) {
			if (largestMatchList < championMatchMap.get(championID).size()) {
				mostPlayedChampionID = championID;
			}
		}
		
		
			ArrayList<Long> matchIdList = (ArrayList<Long>) championMatchMap.get(mostPlayedChampionID);
			int currentMatch = 0;
			int totalMatches =  matchIdList.size();
			
			System.out.println("Only Getting Most Played Champion");
			
			for (long matchID : matchIdList) {
				currentMatch++;
				System.out.println("Match " + currentMatch + "/" + totalMatches);
				RetrievedItemListDto itemListData = matchItems.getMatchItemsForSummoner(matchID, matchHistory.getAccountID());

				if (itemListData != null) {
					
					storeMatchData(
							setMatchData(summonerName, mostPlayedChampionID, matchID, itemListData)
							);
				}
			}
	}
	
	
	public MatchDataDto setMatchData(String summonerName, long championID, long matchID, RetrievedItemListDto itemListData) {
		MatchDataDto matchData = new MatchDataDto();
		matchData.setSummonerName(summonerName);
		matchData.setSummonerID(matchHistory.getSummonerID());
		matchData.setAccountID(matchHistory.getAccountID());
		matchData.setChampionID(championID);
		matchData.setMatchID(matchID);
		matchData.setItemList(itemListData);
		//matchData.printMatchData();
		return matchData;
	}
	
	public void storeMatchData(MatchDataDto matchData) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			String json = ow.writeValueAsString(matchData);
			HttpResponse response = simpleESPost.post(json, "summoners/summoner");
			System.out.println("Status Code: " + response.getStatusCode() + 
								" | Status Text: " + response.getStatusText());

			System.out.println(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

