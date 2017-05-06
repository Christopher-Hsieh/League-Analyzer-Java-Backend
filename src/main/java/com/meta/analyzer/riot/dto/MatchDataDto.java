package com.meta.analyzer.riot.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchDataDto {
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
		System.out.println("Account ID: " + getAccountID());
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
	long accountID;
	private ItemListDto itemList;
	
	Map<String,Long> itemMap = new HashMap<String,Long>();
	
	
	public Map<String, Long> getItemMap() {
		return itemMap;
	}
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
	private ItemListDto getItemList() {
		return itemList;
	}
	
	public void setItemList(ItemListDto itemList) {
		this.itemList = itemList;
		itemMap.clear();
		itemMap.put("item0", itemList.getItemList().get(0));
		itemMap.put("item1", itemList.getItemList().get(1));
		itemMap.put("item2", itemList.getItemList().get(2));
		itemMap.put("item3", itemList.getItemList().get(3));
		itemMap.put("item4", itemList.getItemList().get(4));
		itemMap.put("item5", itemList.getItemList().get(5));
		itemMap.put("item6", itemList.getItemList().get(6));
	}
	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}
	
	public long getAccountID() {
		return accountID;
	}
	
	
	
	
}
