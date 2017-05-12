package com.meta.analyzer.jest;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.analyzer.jest.client.JestClientCreator;
import com.meta.analyzer.jest.dto.ExtractedChampionItemCountDto;
import com.meta.analyzer.jest.dto.ExtractedItemTotalsDto;
import com.meta.analyzer.jest.queries.SummonerItemQueries;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Component
public class AggregateSummonerChampionsAndItems {
	
	@ Autowired
	JestClientCreator clientCreator;
	
	@PostConstruct
	public void extractChampionsAndItems() {
		Search search = SummonerItemQueries.getChampionItemsSearch();
		
	    JestClient client = clientCreator.getJestClient();
	    SearchResult result = null;
		try {
			result = client.execute(search);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    System.out.println("ES Response with aggregation:\n" + result.getJsonString());
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    JsonNode jsonNodeRoot = null;
		try {
			jsonNodeRoot = objectMapper.readTree(result.getJsonString());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	    JsonNode championNodes = jsonNodeRoot.get("aggregations").get("terms").withArray("buckets");
	    ArrayList<ExtractedChampionItemCountDto> extractedChampionItemCounts = extractChampionItemsNodes(championNodes);
	}
	
	/*
	 * Overall extraction method
	 */
	private ArrayList<ExtractedChampionItemCountDto> extractChampionItemsNodes(JsonNode championItemsNodeArray) {
		ArrayList<ExtractedChampionItemCountDto> championItemCountList = new ArrayList<>();
		for(int i = 0; i < championItemsNodeArray.size(); i++) {
			
			int championId = championItemsNodeArray.get(i).get("key").asInt();
			int gamesPlayed = championItemsNodeArray.get(i).get("doc_count").asInt();
			
			JsonNode itemsJsonNode = championItemsNodeArray.get(i).get("terms").get("buckets");
			
			championItemCountList.add(new ExtractedChampionItemCountDto(championId, gamesPlayed, getItemTotalsList (itemsJsonNode)));
			
		}
		return championItemCountList;
	}
	
	/*
	 * Extract Items from a node
	 */
	private ArrayList<ExtractedItemTotalsDto> getItemTotalsList (JsonNode itemsJsonNode) {
		ArrayList<ExtractedItemTotalsDto> itemTotalsList = new ArrayList<>();
		for(int i = 0; i < itemsJsonNode.size(); i++) {
			int itemId = itemsJsonNode.get(i).get("key").asInt();
			int itemCount = itemsJsonNode.get(i).get("doc_count").asInt();
			itemTotalsList.add(new ExtractedItemTotalsDto(itemId, itemCount));
		}
		return itemTotalsList;
	}
	
}
