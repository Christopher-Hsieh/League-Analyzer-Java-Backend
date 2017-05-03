package com.meta.analyzer.riot.api.grabber;

import java.util.ArrayList;

public class MatchData {
//	{
//	    "summonerName": {
//	        "summonerID": "SUMMONER_ID",
//	        "matchID": "MATCH_ID",
//	        "championID": "CHAMPION_ID",
//
//	        "items": {
//	            "item0": "ITEM_ID",
//	            "item1": "ITEM_ID",
//	            "item2": "ITEM_ID",
//	            "item3": "ITEM_ID",
//	            "item4": "ITEM_ID",
//	            "item5": "ITEM_ID",
//	            "item6": "ITEM_ID"
//	        }
//	    }
//	}
	
	public void printMatchData() {
		System.out.println("Summoner Name: " + getSummonerName());
		System.out.println("Summoner ID: " + getSummonerID());
		System.out.println("Champion ID: " + getChampionID());
		System.out.println("Match ID: " + getMatchID());
		
		System.out.println("Item List: ");
		ArrayList<Long> list = itemList.getItemList();
		for (int i = 0; i < list.size(); i++ ) {
			System.out.println("item" + i + list.get(i)); 
		}
	}
	
	String summonerName;
	long summonerID;
	long championID;
	long matchID;
	ItemListData itemList;
	
	public String getSummonerName() {
		return summonerName;
	}
	public void setSummonerName(String summonerName) {
		this.summonerName = summonerName;
	}
	public long getSummonerID() {
		return summonerID;
	}
	public void setSummonerID(long summonerID) {
		this.summonerID = summonerID;
	}
	public long getChampionID() {
		return championID;
	}
	public void setChampionID(long championID) {
		this.championID = championID;
	}
	public long getMatchID() {
		return matchID;
	}
	public void setMatchID(long matchID) {
		this.matchID = matchID;
	}
	public ItemListData getItemList() {
		return itemList;
	}
	public void setItemList(ItemListData itemList) {
		this.itemList = itemList;
	}
	
	
	
	
}
