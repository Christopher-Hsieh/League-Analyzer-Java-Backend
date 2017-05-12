package com.meta.analyzer.riot.api.grabber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meta.analyzer.jest.PullMatchIds;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

/*
 * Class for making calls to Riot Api for match history and match info
 */
@Component
public class GetMatchHistory {
	
	@Autowired
	RiotApi api;
	
	@Autowired
	PullMatchIds pullMatchIds;
	
	private String summonerName;
	private long summonerID;
	private long accountID;
	private Platform platform = Platform.NA;
	int totalMatches;

	//@PostConstruct
	/**
	 * 
	 * @return Map<Champion ID, ArrayList<Match ID>>
	 * @throws RiotApiException
	 */
	public Map<Long, Collection<Long>> getMatchHistory(String newSummonerName)  {
		this.summonerName = newSummonerName;
		/*
		 * Map Champion ID - > List of Match IDs
		 * 	Map<Long, Collection<Long>>
		 *	Map<Champion ID, ArrayList<Match ID>>
		 */
		Map<Long, Collection<Long>> championMatchMap = new HashMap<Long, Collection<Long>>();
		
		try {
			setSummonerID(getSummoner(summonerName).getId());
			setAccountID(getSummoner(summonerName).getAccountId());
		} catch (RiotApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MatchList matchList = null;
		try {
			matchList = api.getMatchListByAccountId(platform, accountID);
		} catch (RiotApiException e) {
			System.out.println("Error Encountered grabbing Champion Match Map in MatchHistory.getMatchHistory()\n" 
					+e.getMessage()+"\n");
			
			e.printStackTrace();
		}
		this.totalMatches = matchList.getTotalGames();
		
		ArrayList<Long> storedMatchIds = pullMatchIds.pull(summonerName);
		
		for (int i =0; i < matchList.getEndIndex(); i++) {
			//Check if matchId is in elastic search already for this summoner
			
			long matchId = matchList.getMatches().get(i).getGameId();
			
			if (storedMatchIds.contains(matchId) == false) {
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


	public Summoner getSummoner(String summonerName) throws RiotApiException {
		Summoner summoner = api.getSummonerByName(platform, summonerName);
		return summoner;
	}
	
	public int getTotalMatches() {
		return totalMatches;
	}
	// Getters and Setters
	public String getSummonerName() {
		return summonerName;
	}

	public void setSummonerName(String summonerName) {
		this.summonerName = summonerName;
	}
	
	private void setAccountID(long accountID) {
		this.accountID = accountID;
	}
	
	public long getAccountID() {
		return accountID;
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