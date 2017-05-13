package com.meta.analyzer.riot.api.grabber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meta.analyzer.riot.dto.RetrievedItemListDto;
import com.meta.analyzer.service.RateManager;

import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;

/**
 * 
 * @author Chris
 * INPUT: Given MatchList and SummonerName
 * OUTPUT: aggregate of all items to champion Ids
 */
@Component
public class GetMatchItems {
	
	@Autowired
	RateManager RateManager;
	
	/*
	 * Methods
	 * getMatchItemsForSummoner(long matchId) - return item list for single match from summoner
	 * getParticipantId(long matchId) - return summonerName's participantId for that match
	 */
	public RetrievedItemListDto getMatchItemsForSummoner(long matchID, long accountID) {

		ParticipantStats stats = RateManager.getParticipantStats(matchID, accountID);
		return new RetrievedItemListDto(stats.getItem0(), stats.getItem1(), stats.getItem2(), 
								stats.getItem3(), stats.getItem4(), stats.getItem5(), stats.getItem6());
	}
	
	
//	public void getItems(long matchId, int participantID) throws RiotApiException {
//		ParticipantStats stats = api.getMatch(platform, matchId).getParticipants().get(participantID-1).getStats();
//		System.out.println("Item List: ");
//		System.out.println(stats.getItem0());
//		System.out.println(stats.getItem1());
//		System.out.println(stats.getItem2());
//		System.out.println(stats.getItem3());
//		System.out.println(stats.getItem4());
//		System.out.println(stats.getItem5());
//		System.out.println(stats.getItem6());
//	}
}
