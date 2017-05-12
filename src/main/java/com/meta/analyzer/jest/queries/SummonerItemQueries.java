package com.meta.analyzer.jest.queries;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.analyzer.jest.client.JestClientCreator;
import com.meta.analyzer.jest.dto.ExtractedChampionItemCountDto;
import com.meta.analyzer.jest.dto.ExtractedItemTotalsDto;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.params.Parameters;

public class SummonerItemQueries {
	
	
	private static final int DOC_BOUND_SIZE = 200;
	
	public static Search getChampionItemsSearch() {
		String summonerName = "firebun";
		String index = "summoners";
		String type = "summoner";
		
		// Use ElasticSearch API to build inital Query
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
	    searchSourceBuilder.query(
	    		QueryBuilders.termQuery(
	    			    "summonerName",    	// field
	    			    summonerName   		// value
	    	)
	     );
	    searchSourceBuilder.size(DOC_BOUND_SIZE);

	    TermsAggregationBuilder terms_agg = AggregationBuilders
	    		.terms(TermsAggregation.TYPE)
	    		.field("championID")
	    		.size(DOC_BOUND_SIZE)
	    		.subAggregation(AggregationBuilders
	    	    		.terms(TermsAggregation.TYPE)
	    	    		.field("itemMap.item0")
	    	    		.size(DOC_BOUND_SIZE));

	    
	    searchSourceBuilder.aggregation(terms_agg);


	    Search search = new Search.Builder(searchSourceBuilder.toString())
	            .addIndex(index)
	            .addType(type)
	            .setParameter(Parameters.SIZE, 0)
	            .build();
	    
	    return search;

	}
}
