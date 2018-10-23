package com.meta.analyzer.jest.queries;

import io.searchbox.core.Index;

public class PutMatchDataQuery {
	
	
	public static Index getPutMatchDataQuery(Object json) {
		
		Index index = new Index.Builder(json).index("summoners").type("summoner").build();

	    return index;

	}
}
