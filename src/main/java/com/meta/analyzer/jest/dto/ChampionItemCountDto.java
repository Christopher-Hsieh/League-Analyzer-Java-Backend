package com.meta.analyzer.jest.dto;

import java.util.ArrayList;


public class ChampionItemCountDto {
	
	int championId;
	int gamesPlayed;
	ArrayList<ItemDto> itemTotalsList;

	public ChampionItemCountDto(int championId, int gamesPlayedAsChampion, ArrayList<ItemDto> itemTotalsList) {
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
	
	public ArrayList<ItemDto> getItemTotalsList() {
		return itemTotalsList;
	}

}
