package com.meta.analyzer.riot.api.grabber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meta.analyzer.riot.dto.RetrievedItemListDto;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;
import net.rithms.riot.constant.Platform;

/**
 * 
 * @author Chris
 * INPUT: Given MatchList and SummonerName
 * OUTPUT: aggregate of all items to champion Ids
 */
@Component
public class GetMatchItems {

	@Autowired
	RiotApi api;
	
	private Platform platform = Platform.NA;
	/*
	 * Methods
	 * getMatchItemsForSummoner(long matchId) - return item list for single match from summoner
	 * getParticipantId(long matchId) - return summonerName's participantId for that match
	 */
	public RetrievedItemListDto getMatchItemsForSummoner(long matchID, long accountID) {

		ParticipantStats stats = retryOnFailureParticipantStats(matchID, accountID);
		return new RetrievedItemListDto(stats.getItem0(), stats.getItem1(), stats.getItem2(), 
								stats.getItem3(), stats.getItem4(), stats.getItem5(), stats.getItem6());
	}
	

	public ParticipantStats retryOnFailureParticipantStats(long matchID, long accountID) {
		int retryCount = 0;
		while (retryCount  < 5) {
			try {
				retryCount++;
				return api.getMatch(platform, matchID).getParticipantByAccountId(accountID).getStats();
			} catch (RiotApiException e) {
				e.getErrorCode();
				System.out.println("Attempt: " + retryCount + " ErrorCode: " + e.getErrorCode() + " Message: " + e.getMessage());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return null;	
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
