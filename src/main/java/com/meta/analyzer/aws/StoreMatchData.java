package com.meta.analyzer.aws;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Request;
import com.amazonaws.SdkBaseException;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.AmazonHttpClient.RequestExecutionBuilder;
import com.meta.analyzer.ApplicationProperties;
import com.meta.analyzer.aws.handlers.SimpleAwsErrorHandler;
import com.meta.analyzer.aws.handlers.SimpleAwsResponseHandler;
import com.meta.analyzer.aws.request.AwsHttpHeaders;
import com.meta.analyzer.aws.request.AwsHttpRequest;
import com.meta.analyzer.aws.request.AwsPost;
import com.meta.analyzer.aws.request.EsHttpRequest;
import com.meta.analyzer.aws.request.SignedRequest;

public class StoreMatchData {

	@Autowired
	ApplicationProperties applicationProperties;
	

    /**
     * Json payload we're about to 
     */
    //private SearchQuery query;
	private String matchDataJson = "Payload ready to be turned into JSON";

    /**
     * Index to search into.
     */
    private String indexName;

    /**
     * Ctor.
     * @param qry
     * @param idxName
     */
//    public AmazonEsSearch(SearchQuery qry, String idxName) {
//        this.query = qry;
//        this.indexName = idxName;
//    }

    /**
     * Perform a search query.
     * @return
     */
 
	public void search() {
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Content-Type", "application/json");
    	AwsHttpRequest<HttpResponse> search =
    	    new SignedRequest<>(
    	        new AwsHttpHeaders<>(
    	            new AwsPost<>(
    	                new EsHttpRequest<>(
    	            	    this.indexName + "/_search",
    	            	    new SimpleAwsResponseHandler(false), new SimpleAwsErrorHandler(false)
    	                ),
    	               // new ByteArrayInputStream(this.query.toJson().toString().getBytes())
    	                new ByteArrayInputStream(this.matchDataJson.getBytes())
    	            ), headers
    	        )
    	    );
        search.getOutput();
        return;
    }
}
