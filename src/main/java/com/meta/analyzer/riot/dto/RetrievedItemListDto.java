package com.meta.analyzer.riot.dto;

import java.util.ArrayList;


public class RetrievedItemListDto {
	
	private ArrayList<Long> itemList = new ArrayList<>();

	public RetrievedItemListDto(
			long item0, long item1, long item2, 
			long item3, long item4, long item5, long item6){
		
		itemList.add(item0);
		itemList.add(item1);
		itemList.add(item2);
		itemList.add(item3);
		itemList.add(item4);
		itemList.add(item5);
		itemList.add(item6);
		

	}

		
		

	
	public ArrayList<Long> getItemList(){
		return this.itemList;
	}
}
