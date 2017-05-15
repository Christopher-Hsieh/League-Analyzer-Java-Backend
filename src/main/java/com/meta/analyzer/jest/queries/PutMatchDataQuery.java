package com.meta.analyzer.jest.queries;

import com.meta.analyzer.riot.dto.MatchDataDto;

import io.searchbox.core.Index;

public class PutMatchDataQuery {
	
	
	public static Index getPutMatchDataQuery(MatchDataDto matchData) {
		
		Index index = new Index.Builder(matchData).index("summoners").type("summoner").build();

	    return index;

	}
}
