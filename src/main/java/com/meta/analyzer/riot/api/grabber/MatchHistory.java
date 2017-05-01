package com.meta.analyzer.riot.api.grabber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.dto.Match.MatchDetail;
import net.rithms.riot.dto.Match.ParticipantIdentity;
import net.rithms.riot.dto.Match.ParticipantStats;
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
	
	String summonerName;
	
	public MatchHistory(String summonerName) {
		this.summonerName = summonerName;
	}
	
	
	@PostConstruct
	/**
	 * 
	 * @return Map<Champion ID, ArrayList<Match ID>>
	 * @throws RiotApiException
	 */
	public Map<Long, Collection<Long>> getMatchHistory() throws RiotApiException {
		
		/*
		 * Map Champion ID - > List of Match IDs
		 * 	Map<Long, Collection<Long>>
		 *	Map<Champion ID, ArrayList<Match ID>>
		 */
		Map<Long, Collection<Long>> championMatchMap = new HashMap<Long, Collection<Long>>();
		
		MatchList matchList = api.getMatchList(getSummoner().getId());
		
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
	
	
	// TODO make dynamic setting of name for calls
	public Summoner getSummoner() throws RiotApiException {
		Summoner summoner = api.getSummonerByName(summonerName);
		return summoner;
	}
	
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
