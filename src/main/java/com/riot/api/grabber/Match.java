package com.riot.api.grabber;

import javax.annotation.PostConstruct;

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
public class Match {

	@Autowired
	RiotApi api;
	
	@PostConstruct
	public void getMatchHistory() throws RiotApiException {
		
		MatchList matchList = api.getMatchList(getSummonerName().getId());
		MatchReference matchReference = matchList.getMatches().get(0);
		System.out.println(matchReference.toString());
	}
	
	public Summoner getSummonerName() throws RiotApiException {
		Summoner in = api.getSummonerByName("firebun");
		System.out.println(in.getId());
		return in;
	}
}
