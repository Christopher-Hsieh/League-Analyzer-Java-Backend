package com.meta.analyzer.jest;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meta.analyzer.jest.client.JestClientCreator;
import com.meta.analyzer.jest.queries.PutMatchDataQuery;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

@Component
public class PutMatchData {
	
	@ Autowired
	JestClientCreator clientCreator;

	static Logger logger = Logger.getLogger(AggregateChampionItems.class.getName());
	
	public int put(Object json) {
		Index index = PutMatchDataQuery.getPutMatchDataQuery(json);
		
	    JestClient client = clientCreator.getJestClient();

		DocumentResult result = null;
		try {
			result  = client.execute(index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    logger.info("ES Response with put:\n" + result.getJsonString());

		return result.getResponseCode();
	}
}
