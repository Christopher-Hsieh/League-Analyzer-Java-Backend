package com.meta.analyzer.jest.dto;

import java.util.ArrayList;


public class ExtractedChampionItemCountDto {
	
	int championId;
	int gamesPlayed;
	ArrayList<ExtractedItemTotalsDto> itemTotalsList;

	public ExtractedChampionItemCountDto(int championId, int gamesPlayedAsChampion, ArrayList<ExtractedItemTotalsDto> itemTotalsList) {
		this.itemTotalsList = itemTotalsList;
		this.championId = championId;
		this.gamesPlayed = gamesPlayedAsChampion;
	}
	
	public int getChampionId() {
		return championId;
	}
	
	public int getGamesPlayedAsChampion() {
		return gamesPlayed;
	}
}
