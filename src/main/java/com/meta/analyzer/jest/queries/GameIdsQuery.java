package com.meta.analyzer.jest.queries;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import io.searchbox.core.Search;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.params.Parameters;

public class GameIdsQuery {
	
	private static final int DOC_BOUND_SIZE = 20000;
	
	public static Search getMatchIdsSearch(String summonerName) {
		String index = "summoners";
		String type = "summoner";
		
		// Use ElasticSearch API to build inital Query
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
	    searchSourceBuilder.query(
	    		QueryBuilders.termQuery(
	    			    "summonerName",    	// field
	    			    summonerName.toLowerCase()   		// value
	    	)
	     );
	    searchSourceBuilder.size(DOC_BOUND_SIZE);

	    TermsAggregationBuilder terms_agg = AggregationBuilders
	    		.terms(TermsAggregation.TYPE)
	    		.field("gameId")
	    		.size(DOC_BOUND_SIZE);

	    
	    searchSourceBuilder.aggregation(terms_agg);


	    Search search = new Search.Builder(searchSourceBuilder.toString())
	            .addIndex(index)
	            .addType(type)
	            .setParameter(Parameters.SIZE, 0)
	            .build();
	    
	    return search;

	}
}
