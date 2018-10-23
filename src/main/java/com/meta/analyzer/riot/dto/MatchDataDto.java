package com.meta.analyzer.riot.dto;

import java.util.List;

import net.rithms.riot.api.endpoints.match.dto.Mastery;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;
import net.rithms.riot.api.endpoints.match.dto.Rune;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

public class MatchDataDto {

	public MatchDataDto(Match match, Summoner summoner) {

		// Match 
		this.gameId = match.getGameId();
		this.gameCreation = match.getGameCreation();
		// Participant
		Participant participant = match.getParticipantByAccountId(summoner.getAccountId());
		this.championId = participant.getChampionId();
		this.runes = participant.getRunes();
		this.spell1Id = participant.getSpell1Id();
		this.spell2Id = participant.getSpell2Id();
		this.masteries = participant.getMasteries();
		
		// Participant Stats
		ParticipantStats participantStats = participant.getStats();
		this.item0 = participantStats.getItem0();
		this.item1 = participantStats.getItem1();
		this.item2 = participantStats.getItem2();
		this.item3 = participantStats.getItem3();
		this.item4 = participantStats.getItem4();
		this.item5 = participantStats.getItem5();
		this.item6 = participantStats.getItem6();
		this.win = participantStats.isWin();
		
		// Summoner
		this.summonerName = summoner.getName();
		this.summonerId = summoner.getId();
		this.accountId = summoner.getAccountId();
		this.profileIconId = summoner.getProfileIconId();
		// Participant Timeline
		//this.role = match.getParticipantIdentities().get(participantId);
	}
	// Match
	private long gameId;
	private long gameCreation;
	
	// Participant
	private int championId;
	private List<Rune> runes;
	private int spell1Id;
	private int spell2Id;
	private List<Mastery> masteries;
	
	// Participant Stats
	private int item0;
	private int item1;
	private int item2;
	private int item3;
	private int item4;
	private int item5;
	private int item6;
	private boolean win;
	
	// Summoner
	private String summonerName;
	private long summonerId;
	private long accountId;
	private long profileIconId;
	
	// Participant Timeline
	//private String role;
	
	public long getGameId() {
		return gameId;
	}
	public long getGameCreation() {
		return gameCreation;
	}
	public int getChampionId() {
		return championId;
	}
	public List<Rune> getRunes() {
		return runes;
	}
	public int getSpell1Id() {
		return spell1Id;
	}
	public int getSpell2Id() {
		return spell2Id;
	}
	public List<Mastery> getMasteries() {
		return masteries;
	}
	public int getItem0() {
		return item0;
	}
	public int getItem1() {
		return item1;
	}
	public int getItem2() {
		return item2;
	}
	public int getItem3() {
		return item3;
	}
	public int getItem4() {
		return item4;
	}
	public int getItem5() {
		return item5;
	}
	public int getItem6() {
		return item6;
	}
	public boolean isWin() {
		return win;
	}
	public String getSummonerName() {
		return summonerName;
	}
	public long getSummonerId() {
		return summonerId;
	}
	public long getAccountId() {
		return accountId;
	}
	public long getProfileIconId() {
		return profileIconId;
	}
}
