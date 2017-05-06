package com.meta.analyzer.riot.api.grabber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;
import net.rithms.riot.constant.Platform;

/**
 * 
 * @author Chris
 * INPUT: Given MatchList and SummonerName
 * OUTPUT: aggregate of all items to champion Ids
 */
@Component
public class MatchItems {

	@Autowired
	RiotApi api;
	
	private Platform platform = Platform.NA;
	/*
	 * Methods
	 * getMatchItemsForSummoner(long matchId) - return item list for single match from summoner
	 * getParticipantId(long matchId) - return summonerName's participantId for that match
	 */
	public ItemListData getMatchItemsForSummoner(long matchID, String summonerName) throws RiotApiException {
		//int participantID = getParticipantID(matchID, summonerName);
		
		

		Participant participant = api.getMatch(platform, matchID).getParticipantBySummonerName(summonerName);
		// Name change maybe, handle it
		if (participant == null) {
			return null;
		}
		ParticipantStats stats = api.getMatch(platform, matchID).getParticipantBySummonerName(summonerName).getStats();
		//.getMatch(matchID).getParticipants().get(participantID-1).getStats();
		return new ItemListData(stats.getItem0(), stats.getItem1(), stats.getItem2(), 
								stats.getItem3(), stats.getItem4(), stats.getItem5(), stats.getItem6());
	}
	
	public void getItems(long matchId, int participantID) throws RiotApiException {
		ParticipantStats stats = api.getMatch(platform, matchId).getParticipants().get(participantID-1).getStats();
		System.out.println("Item List: ");
		System.out.println(stats.getItem0());
		System.out.println(stats.getItem1());
		System.out.println(stats.getItem2());
		System.out.println(stats.getItem3());
		System.out.println(stats.getItem4());
		System.out.println(stats.getItem5());
		System.out.println(stats.getItem6());
	}
	

//	public int getParticipantID(long matchId, String summonerName) throws RiotApiException {
//		MatchDetail detail = api.getMatch(matchId);
//
//		List<ParticipantIdentity> participantIdentitiesList = detail.getParticipantIdentities();
//		for (ParticipantIdentity identity : participantIdentitiesList) {
//			if(identity.getPlayer().getSummonerName().toLowerCase().equals(summonerName)) {
//				return identity.getParticipantId();
//			}
//		}
//		// Returning -1 means that the user likely changed their name, and their new name is appearing in the match we are searching
//		return -1;
//	}
}
