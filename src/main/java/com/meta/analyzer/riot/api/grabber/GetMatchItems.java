package com.meta.analyzer.riot.api.grabber;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

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
@Scope("prototype")
public class GetMatchItems {
	
    @Autowired
    private WebApplicationContext context;
    
    RateManager rateManager;
    
	@PostConstruct
    public void init() {
    	this.rateManager = getRateManager();
    }
    
    public RateManager getRateManager() {
        return (RateManager) context.getBean("rateManager");
    }
	
	/*
	 * Methods
	 * getMatchItemsForSummoner(long matchId) - return item list for single match from summoner
	 * getParticipantId(long matchId) - return summonerName's participantId for that match
	 */
	public RetrievedItemListDto getMatchItemsForSummoner(long matchID, long accountID) {

		ParticipantStats stats = rateManager.getParticipantStats(matchID, accountID);
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
