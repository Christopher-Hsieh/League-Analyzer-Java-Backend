package com.meta.analyzer.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

@Component
@Scope("prototype")
public class RateManager {
	
	@Autowired
	RiotApi api;
	
	@Autowired
	LinkedList<LocalDateTime> apiCallHistory;
	
//	@Autowired
//	@Qualifier(value="riotApiLock")
//	Object riotApiLock;
	
	private Platform platform = Platform.NA;
	
	public ParticipantStats getParticipantStats(long matchId, long accountID)  {
		ParticipantStats stats = null;
		try {
			rateLimit();
			stats = api.getMatch(platform, matchId).getParticipantByAccountId(accountID).getStats();
		} catch (InterruptedException | RiotApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stats;
	}
	
	public Match getMatch(long matchId, long accountID)  {
		Match match = null;
		try {
			rateLimit();
			match = api.getMatch(platform, matchId);
		} catch (InterruptedException | RiotApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return match;
	}
	
	public MatchList getMatchList(long accountID) {
		MatchList matchList = null;
		try {
			rateLimit();
			matchList = api.getMatchListByAccountId(platform, accountID);
		} catch (InterruptedException | RiotApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return matchList;
	}
	
	public Summoner getSummonerByName(String summonerName)  {
		Summoner summoner = null;
		try {
			rateLimit();
			summoner = api.getSummonerByName(platform, summonerName);
		} catch (InterruptedException | RiotApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return summoner;
	}
	
	public void rateLimit() throws InterruptedException {
		LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
			if (apiCallHistory.size() < 9) {
				apiCallHistory.addFirst(currentTime);
				return;
			}
			else {
				Duration duration = Duration.between(apiCallHistory.getLast(), LocalDateTime.now(ZoneOffset.UTC));
				// If the 10th call was less 10 seconds ago
				//		then wait however long until 10 seconds
				if(duration.toMillis() < 10000) {
					Thread.sleep(11000 - duration.toMillis());
				}
				apiCallHistory.removeLast();
			}
			
			apiCallHistory.addFirst(currentTime);
	}
}
