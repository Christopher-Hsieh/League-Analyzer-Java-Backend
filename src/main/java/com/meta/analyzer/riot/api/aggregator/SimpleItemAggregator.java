package com.meta.analyzer.riot.api.aggregator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meta.analyzer.aws.StoreMatchData;
import com.meta.analyzer.riot.api.grabber.MatchHistory;
import com.meta.analyzer.riot.api.grabber.MatchItems;
import com.meta.analyzer.riot.dto.ItemListDto;
import com.meta.analyzer.riot.dto.MatchDataDto;

import net.rithms.riot.api.RiotApiException;

// Given summoner Name
// Get Item %'s for all champions
/*
 * Get an entire summoners match history worth of items
 */
@Component
public class SimpleItemAggregator {

	@Autowired
	MatchHistory matchHistory;
	
	@Autowired
	MatchItems matchItems;

	@Autowired
	StoreMatchData storeMatchData;
	
	//@PostConstruct
	public void testSetMatchData() throws RiotApiException {
		setMatchData("firebun");
		//storeMatchData.postMatchData("{\"field\":\"fieldValue\"}","index/id");
	}
	
	public void setMatchData(String summonerName) throws RiotApiException {
		/*
		 * MatchHistory.getMatchHistory => Map<Champion ID, ArrayList<Match ID>>
		 * (MatchData)String summonerName; => Input to function 
		 * (MatchData)long summonerID; => MatchHistory.getSummonerId()
		 */
		MatchDataDto matchData = new MatchDataDto();
		matchData.setSummonerName(summonerName);
		Map<Long, Collection<Long>> championMatchMap = null;
		championMatchMap = matchHistory.getMatchHistory(summonerName);
		matchData.setSummonerID(matchHistory.getSummonerID());
		matchData.setAccountID(matchHistory.getAccountID());
		/*
		 * for ( All ChampionIDs in Map<Champion ID, ArrayList<Match ID>>) {
		 * 		getMatchItemsForSummoner(long matchId) - return item list for single match from summoner 
		 * 		(MatchData)long championID; 
		 * 		(MatchData)long matchID; 
		 * 		(MatchData)ItemListData itemList; Do something with
		 * 		Push MatchData Object to ES?
		 * } //end for
		 *
		 * done
		 */
		if (championMatchMap.isEmpty()) {
			System.out.println("No Champions found in match History!");
			return;
		}
		int currentMatch = 0;
		int totalMatches = matchHistory.getTotalMatches();
		int foundMatches = 0;
		for (long championID : championMatchMap.keySet()) {
				ArrayList<Long> matchIdList = (ArrayList<Long>) championMatchMap.get(championID);
				for (long matchID : matchIdList) {
					currentMatch++;
					System.out.println("Match " + currentMatch + "/" + totalMatches);
					ItemListDto itemListData = matchItems.getMatchItemsForSummoner(matchID, matchHistory.getAccountID());
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (itemListData != null) {
						matchData.setChampionID(championID);
						matchData.setMatchID(matchID);
						matchData.setItemList(itemListData);
						//matchData.printMatchData();
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						try {
							String json = ow.writeValueAsString(matchData);
							HttpResponse response = storeMatchData.postMatchData(json, "summoners/summoner");
							System.out.println("Status Code: " + response.getStatusCode() + 
												" | Status Text: " + response.getStatusText() +
												" | Found Matches: " + ++foundMatches);
//							try {
//								System.out.println("Response Content: " + IOUtils.toString(response.getContent()));
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							System.out.println(json);
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		}

	}
}
