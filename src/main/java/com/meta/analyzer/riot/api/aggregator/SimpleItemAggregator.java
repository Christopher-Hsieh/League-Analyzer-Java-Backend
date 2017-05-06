package com.meta.analyzer.riot.api.aggregator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meta.analyzer.riot.api.grabber.ItemListData;
import com.meta.analyzer.riot.api.grabber.MatchData;
import com.meta.analyzer.riot.api.grabber.MatchHistory;
import com.meta.analyzer.riot.api.grabber.MatchItems;

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

	@PostConstruct
	public void testSetMatchData() throws RiotApiException {
		setMatchData("firebun");
	}
	
	public void setMatchData(String summonerName) throws RiotApiException {
		/*
		 * MatchHistory.getMatchHistory => Map<Champion ID, ArrayList<Match ID>>
		 * (MatchData)String summonerName; => Input to function 
		 * (MatchData)long summonerID; => MatchHistory.getSummonerId()
		 */
		MatchData matchData = new MatchData();
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
		
		for (long championID : championMatchMap.keySet()) {
				ArrayList<Long> matchIdList = (ArrayList<Long>) championMatchMap.get(championID);
				for (long matchID : matchIdList) {
					ItemListData itemListData = matchItems.getMatchItemsForSummoner(matchID, summonerName);
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (itemListData != null) {
						matchData.setChampionID(championID);
						matchData.setMatchID(matchID);
						matchData.setItemList(itemListData);
						matchData.printMatchData();
					}
				}
		}

	}
}
