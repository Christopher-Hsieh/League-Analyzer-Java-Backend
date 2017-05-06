
/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1)Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  3)Neither the name of charles-rest nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.meta.analyzer.aws;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.http.HttpResponse;
import com.meta.analyzer.aws.dto.MatchDataSearchResultsDto;
import com.meta.analyzer.aws.handlers.SimpleAwsErrorHandler;
import com.meta.analyzer.aws.handlers.SimpleAwsResponseHandler;
import com.meta.analyzer.aws.request.AwsHttpHeaders;
import com.meta.analyzer.aws.request.AwsHttpRequest;
import com.meta.analyzer.aws.request.AwsPost;
import com.meta.analyzer.aws.request.EsHttpRequest;
import com.meta.analyzer.aws.request.SignedRequest;

/**
 * Perform a search in the Amazon ElasticSerch service
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class SampleEsPost {

    /**
     * ElasticSearch  query.
     */
    //private SearchQuery query;
	private String query = "query string here";

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
    	                new ByteArrayInputStream(this.query.getBytes())
    	            ), headers
    	        )
    	    );
        search.getOutput();
        return;
    }
}