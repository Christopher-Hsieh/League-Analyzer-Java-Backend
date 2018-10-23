package com.meta.analyzer.riot.api.grabber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meta.analyzer.jest.PullGameIds;
import com.meta.analyzer.service.RateManager;

import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

/*
 * Class for making calls to Riot Api for match history and match info
 */
@Component
public class GetMatchHistory {	
    
    @Autowired
    RateManager rateManager;
    
    @Autowired
	PullGameIds pullMatchIds;
	
	int totalMatches;

	/**
	 * 
	 * @return Map<Champion ID, ArrayList<Match ID>>
	 * @throws RiotApiException
	 */
	public Map<Long, Collection<Long>> getMatchHistory(Summoner summoner)  {


		Map<Long, Collection<Long>> championMatchMap = new HashMap<Long, Collection<Long>>();
		

		MatchList matchList = rateManager.getMatchList(summoner.getAccountId());
		ArrayList<Long> storedMatchIds = pullMatchIds.pull(summoner.getName());
		
		if (matchList == null) {
			return null;
		}
		if (storedMatchIds == null) {
			storedMatchIds = new ArrayList<Long>();
			storedMatchIds.add((long) -1);
		}
		
		this.totalMatches = matchList.getTotalGames();

		for (int i =0; i < matchList.getEndIndex(); i++) {
			//Check if matchId is in elastic search already for this summoner
			
			long matchId = matchList.getMatches().get(i).getGameId();
			
			if (storedMatchIds == null || storedMatchIds.contains(matchId) == false) {
				long championId = matchList.getMatches().get(i).getChampion();

				if (championMatchMap.containsKey(championId)) {
					// Key exist, just add to arrayList
					championMatchMap.get(championId).add(matchId);
				}
				else {
					// Key not present. Create arrayList and add to it.
					championMatchMap.put(championId, new ArrayList<Long>());
					championMatchMap.get(championId).add(matchId);
				}
			}
		}
		// printChampionMatchMap(championMatchMap);
		return championMatchMap;
	}
	
	public int getTotalMatches() {
		return totalMatches;
	}

	
	// Print functions for Debugging
	public void printChampionMatchMap( Map<Long, Collection<Long>> championMatchMap) throws RiotApiException {

		System.out.println("---- Printing HashMap ----");
		for (long i = 0; i < 2; i++) {
			if (championMatchMap.containsKey(i)) {
				System.out.println("ChampionId: " + i +" -> " );
				ArrayList<Long> matchIdList = (ArrayList<Long>) championMatchMap.get(i);
				for (long matchId : matchIdList) {
					System.out.print(matchId + ", ");
				}
			}
		}
		System.out.println("---- End HashMap ----");
	}
	
	public void printMatchReference(MatchReference reference) {
		//System.out.println("Match ID:" + reference.getMatchId());
		System.out.println("TimeStamp:" + reference.getTimestamp());
		System.out.println("Champion:" + reference.getChampion());
		System.out.println("Lane:" + reference.getLane());
		System.out.println("Platform ID:" + reference.getPlatformId());
		System.out.println("Queue:" + reference.getQueue());
		System.out.println("Role:" + reference.getRole());
		System.out.println("Season:" + reference.getSeason());
	}

}
