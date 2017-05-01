package com.meta.analyzer.riot.api.grabber;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.dto.Match.MatchDetail;
import net.rithms.riot.dto.Match.ParticipantIdentity;
import net.rithms.riot.dto.Match.ParticipantStats;

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
	
	Map<Long, Collection<Long>> championMatchMap;
	String summonerName;
	
	/*
	 * Methods
	 * getMatchHistoryItems() - Get an entire summoners match history worth of items
	 * getMatchItemsForSummoner(long matchId) - return item list for single match from summoner
	 * getParticipantId(long matchId) - return summonerName's participantId for that match
	 */
	
	/*
	 * Get an entire summoners match history worth of items
	 * MatchId, Items
	 */
	public void getEntireMatchHistoryItems() {

	}

	public MatchItems(Map<Long, Collection<Long>> championMatchMap, String summonerName) {
		this.championMatchMap = new HashMap<Long, Collection<Long>>(championMatchMap);
		this.summonerName = summonerName;
	}
	
	public void getItems(long matchId, int participantId) throws RiotApiException {
		ParticipantStats stats = api.getMatch(matchId).getParticipants().get(participantId).getStats();
		System.out.println("Item List: ");
		System.out.println(stats.getItem0());
		System.out.println(stats.getItem1());
		System.out.println(stats.getItem2());
		System.out.println(stats.getItem3());
		System.out.println(stats.getItem4());
		System.out.println(stats.getItem5());
		System.out.println(stats.getItem6());
	}
	

	public int getParticipantId(long matchId) throws RiotApiException {
		MatchDetail detail = api.getMatch(matchId);

		List<ParticipantIdentity> participantIdentitiesList = detail.getParticipantIdentities();
		for (ParticipantIdentity identity : participantIdentitiesList) {
			if(identity.getPlayer().getSummonerName().toLowerCase().equals(summonerName)) {
				return identity.getParticipantId();
			}
		}
		return -1;
	}
}