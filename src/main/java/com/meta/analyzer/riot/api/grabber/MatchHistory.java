package com.meta.analyzer.riot.api.grabber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.dto.MatchList.MatchList;
import net.rithms.riot.dto.MatchList.MatchReference;
import net.rithms.riot.dto.Summoner.Summoner;

/*
 * Class for making calls to Riot Api for match history and match info
 */
@Component
public class MatchHistory {
	
	@Autowired
	RiotApi api;
	
	private String summonerName;
	private long summonerID;

	//@PostConstruct
	/**
	 * 
	 * @return Map<Champion ID, ArrayList<Match ID>>
	 * @throws RiotApiException
	 */
	public Map<Long, Collection<Long>> getMatchHistory(String newSummonerName) throws RiotApiException {
		this.summonerName = newSummonerName;
		/*
		 * Map Champion ID - > List of Match IDs
		 * 	Map<Long, Collection<Long>>
		 *	Map<Champion ID, ArrayList<Match ID>>
		 */
		Map<Long, Collection<Long>> championMatchMap = new HashMap<Long, Collection<Long>>();
		setSummonerID(getSummoner(summonerName).getId());
		MatchList matchList = api.getMatchList(summonerID);
		
		for (int i =0; i < matchList.getEndIndex(); i++) {
			// printMatchReference(matchList.getMatches().get(i));
			long matchId = matchList.getMatches().get(i).getMatchId();
			long championId = matchList.getMatches().get(i).getChampion();
			//System.out.println("Key: " + championId);
			
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
		// printChampionMatchMap(championMatchMap);
		return championMatchMap;
	}
	
	
	public Summoner getSummoner(String summonerName) throws RiotApiException {
		Summoner summoner = api.getSummonerByName(summonerName);
		return summoner;
	}
	
	
	// Getters and Setters
	public String getSummonerName() {
		return summonerName;
	}


	public void setSummonerName(String summonerName) {
		this.summonerName = summonerName;
	}


	public long getSummonerID() {
		return summonerID;
	}


	public void setSummonerID(long summonerID) {
		this.summonerID = summonerID;
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
		System.out.println("Match ID:" + reference.getMatchId());
		System.out.println("TimeStamp:" + reference.getTimestamp());
		System.out.println("Champion:" + reference.getChampion());
		System.out.println("Lane:" + reference.getLane());
		System.out.println("Platform ID:" + reference.getPlatformId());
		System.out.println("Queue:" + reference.getQueue());
		System.out.println("Role:" + reference.getRole());
		System.out.println("Season:" + reference.getSeason());
	}
}
