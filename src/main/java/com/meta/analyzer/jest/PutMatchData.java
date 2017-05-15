package com.meta.analyzer.jest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meta.analyzer.jest.client.JestClientCreator;
import com.meta.analyzer.jest.queries.PutMatchDataQuery;
import com.meta.analyzer.riot.dto.MatchDataDto;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

@Component
public class PutMatchData {
	
	@ Autowired
	JestClientCreator clientCreator;

	
	public int put(MatchDataDto matchData) {
		Index index = PutMatchDataQuery.getPutMatchDataQuery(matchData);
		
	    JestClient client = clientCreator.getJestClient();

		DocumentResult result = null;
		try {
			result  = client.execute(index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    System.out.println("ES Response with put:\n" + result.getJsonString());

		return result.getResponseCode();
	}
}
