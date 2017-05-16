package com.meta.analyzer.jest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.analyzer.jest.client.JestClientCreator;
import com.meta.analyzer.jest.dto.ChampionItemCountDto;
import com.meta.analyzer.jest.dto.ItemDto;
import com.meta.analyzer.jest.queries.SummonerItemQuery;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Component
public class AggregateChampionItems {
	
	@Autowired
	JestClientCreator clientCreator;
	
	static Logger logger = Logger.getLogger(AggregateChampionItems.class.getName());
	String[] itemField = new String[]{"item0", "item1", "item2", "item3", "item4", "item5", "item6"};
	

	// Just does one champion right now
	public ChampionItemCountDto extractChampionItems(String summonerName) throws IOException {
	
	    JestClient client = clientCreator.getJestClient();
	    int championId = -1;
	    int gamesPlayed = -1;
		/* for 1 - 6
		 * 	Get search for itemN
		 * 	Parse for itemsList
		 * 	Add most used item not already on the list
		 * 
		 * return list of items
		 */
	    
	    Map<Integer, Integer> itemCounts = new HashMap<Integer, Integer>();
	    //ChampionItemCountDto championItemCountDto = new ChampionItemCountDto();
	    for (int i = 0; i < itemField.length; i++) {
	    	// Get search for ItemN
		    Search search = SummonerItemQuery.getChampionItemsSearch(summonerName, itemField[i]);
		    SearchResult result = client.execute(search);
		    ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNodeRoot = objectMapper.readTree(result.getJsonString());

		    // Parse for itemsList
		    JsonNode championNodes = jsonNodeRoot.get("aggregations").get("terms").withArray("buckets");
		    JsonNode itemsJsonNode = championNodes.get(0).get("terms").get("buckets");
		    
		    // Add most used item not already on the list
		   itemCounts = addOrIncrementItems(itemCounts, itemsJsonNode);
		   
		   // Set champion id and games played
			championId = championNodes.get(0).get("key").asInt();
			gamesPlayed = championNodes.get(0).get("doc_count").asInt();
	    }
	    
	    ArrayList<ItemDto> itemTotals = new ArrayList<>();
	    
	    for (Map.Entry<Integer, Integer> entry : itemCounts.entrySet()) {
	    	System.out.println(entry.getKey() + "/" + entry.getValue());
	    	if (entry.getKey() != 0) { // Skip no item in slot
	    		itemTotals.add(new ItemDto(entry.getKey(), entry.getValue()));
	    	}
	    }
	    

	    // Sort by most used item
	    Collections.sort(itemTotals, new Comparator<ItemDto>() {

	        public int compare(ItemDto o1, ItemDto o2) {
	            return o2.getItemCount() - o1.getItemCount();
	        }
	    });
	    
	    return new ChampionItemCountDto(championId, gamesPlayed, itemTotals);
	}
	
	public Map<Integer, Integer> addOrIncrementItems(Map<Integer, Integer> itemCounts, JsonNode itemsJsonNode) {
		for(int i = 0; i < itemsJsonNode.size() -1; i++) {
			int itemId = itemsJsonNode.get(i).get("key").asInt();
			int itemCount = itemsJsonNode.get(i).get("doc_count").asInt();
			
			if (itemCounts.containsKey(itemId)) {
				int prevItemTotal = itemCounts.get(itemId);
				//itemCounts.remove(itemId);
				//itemCounts = removeEntryInMap(itemCounts, itemId);
				itemCounts.replace(itemId, prevItemTotal, prevItemTotal + itemCount);
			}
			else {
				itemCounts.put(itemId, itemCount);
			}
		}
		return itemCounts;
		
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * Overall extraction method
	 */
	private ArrayList<ChampionItemCountDto> extractChampionItemsNodes(JsonNode championItemsNodeArray) {
		ArrayList<ChampionItemCountDto> championItemCountList = new ArrayList<>();
		int championId = championItemsNodeArray.get(0).get("key").asInt();
		int gamesPlayed = championItemsNodeArray.get(0).get("doc_count").asInt();
			
		JsonNode itemsJsonNode = championItemsNodeArray.get(0).get("terms").get("buckets");
			
		//championItemCountList.add(new ExtractedChampionItemCountDto(championId, gamesPlayed, getItemTotalsList (itemsJsonNode)));

		return championItemCountList;
	}
	
	/*
	 * Extract Items from a node
	 */
//	private ArrayList<ItemTotalsDto> getItemTotalsList (JsonNode itemsJsonNode) {
//		ArrayList<ItemTotalsDto> itemTotalsList = new ArrayList<>();
//		for(int i = 0; i < itemsJsonNode.size(); i++) {
//			int itemId = itemsJsonNode.get(i).get("key").asInt();
//			int itemCount = itemsJsonNode.get(i).get("doc_count").asInt();
//			itemTotalsList.add(new ItemTotalsDto(itemId, itemCount));
//		}
//		return itemTotalsList;
//	}
	
}
