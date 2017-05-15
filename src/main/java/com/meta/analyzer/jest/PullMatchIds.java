package com.meta.analyzer.jest;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.analyzer.jest.client.JestClientCreator;
import com.meta.analyzer.jest.queries.MatchIdsQuery;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Component
@Scope("prototype") // Means every time this bean is requested. 
public class PullMatchIds {
	
	@ Autowired
	JestClientCreator clientCreator;

	
	public ArrayList<Long> pull(String summonerName) {
		Search search = MatchIdsQuery.getMatchIdsSearch(summonerName);
		
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
		ArrayList<Long> matchIds = new ArrayList<>();
		
		if (!result.isSucceeded()) {
			return null;
		}
		JsonNode node = jsonNodeRoot.get("aggregations").get("terms").withArray("buckets");
		
		for(int i = 0; i < node.size(); i++) {
			matchIds.add(node.get(i).get("key").asLong());
		}
		return matchIds;
	}
}
