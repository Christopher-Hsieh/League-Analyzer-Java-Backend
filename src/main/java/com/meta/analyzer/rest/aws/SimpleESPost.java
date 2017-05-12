package com.meta.analyzer.rest.aws;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.http.HttpResponse;
import com.meta.analyzer.ApplicationProperties;
import com.meta.analyzer.rest.aws.handlers.SimpleAwsErrorHandler;
import com.meta.analyzer.rest.aws.handlers.SimpleAwsResponseHandler;
import com.meta.analyzer.rest.aws.request.AwsHttpHeaders;
import com.meta.analyzer.rest.aws.request.AwsHttpRequest;
import com.meta.analyzer.rest.aws.request.AwsPost;
import com.meta.analyzer.rest.aws.request.EsHttpRequest;
import com.meta.analyzer.rest.aws.request.SignedRequest;

@Component
public class SimpleESPost {

	@Autowired
	ApplicationProperties applicationProperties;
	
	
    /**
     * Json payload we're about to 
     * String jsonString
     *
     * Perform a search query.
     * @return 
     * @return
     */
 
	public HttpResponse post(String jsonString, String idxName) {
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Content-Type", "application/json");
    	AwsHttpRequest<HttpResponse> response =
    	    new SignedRequest<>(
    	        new AwsHttpHeaders<>(
    	            new AwsPost<>(
    	                new EsHttpRequest<>(
    	            	    idxName, //+ "/_search",
    	            	    new SimpleAwsResponseHandler(false), 
    	            	    new SimpleAwsErrorHandler(false),
    	            	    applicationProperties
    	                ),
    	               // new ByteArrayInputStream(this.query.toJson().toString().getBytes())
    	                new ByteArrayInputStream(jsonString.getBytes())
    	            ), headers
    	        ), applicationProperties
    	    );
        return response.getOutput(); 
    }
}
