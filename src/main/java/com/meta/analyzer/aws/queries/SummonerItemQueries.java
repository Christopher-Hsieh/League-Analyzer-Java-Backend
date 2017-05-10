package com.meta.analyzer.aws.queries;

import javax.annotation.PostConstruct;

import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.google.common.base.Supplier;
import com.meta.analyzer.ApplicationProperties;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.params.Parameters;
import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

@Component
public class SummonerItemQueries {
	@Autowired
	ApplicationProperties applicationProperties;
	
	public static final String CHAMPION_BUILDS_QUERY = "";
	
	@PostConstruct
	public void me() {
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

	    JestClient client = getJestClient(); // just a private method creating a client the way Jest client is created

	    TermsAggregationBuilder terms_agg = AggregationBuilders
	    		.terms(TermsAggregation.TYPE)
	    		.field("itemMap.item0")
	    		.subAggregation(AggregationBuilders
	    	    		.terms(TermsAggregation.TYPE)
	    	    		.field("itemMap.item1"));

	    
	    searchSourceBuilder.aggregation(terms_agg);


	    Search search = new Search.Builder(searchSourceBuilder.toString())
	            .addIndex(index)
	            .addType(type)
	            .setParameter(Parameters.SIZE, 0)
	            .build();
	    System.out.println(search.toString());
	    SearchResult result = null;
		try {
			result = client.execute(search);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("ES Response with aggregation:\n" + result.getJsonString());
	}
	
	private JestClientFactory getJestClientFactory() {

	    return new JestClientFactory() {

	        @Override
	        protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
	            builder.addInterceptorLast(prepareInterceptor());
	            return builder;
	        }

//	        @Override
//	        protected HttpAsyncClientBuilder configureHttpClient(HttpAsyncClientBuilder builder) {
//	            builder.addInterceptorLast(prepareInterceptor());
//	            return builder;
//	        }
	    };
	}
	
	private AWSSigningRequestInterceptor prepareInterceptor() {
		final Supplier<LocalDateTime> clock = () -> LocalDateTime.now(ZoneOffset.UTC);
		final AWSSigner awsSigner = new AWSSigner(createCredentialsProvider(), applicationProperties.getEsRegion(), applicationProperties.getEsServiceName(), clock);
	    return new AWSSigningRequestInterceptor(awsSigner);
	}
	
	public JestClient getJestClient() {

	    final JestClientFactory factory = getJestClientFactory();

	    factory.setHttpClientConfig(new HttpClientConfig
	            .Builder(applicationProperties.getEsEndpoint())
	            //.multiThreaded(true)
	            .build());
	    
	    return factory.getObject();
	}
	    

	public AWSCredentialsProvider createCredentialsProvider() {
		return new StaticCredentialsProvider(new AwsCredentialsFromSystem());
	}
    /**
     * AWS credentials (aws access key id and aws secret key from the system properties).
     */
    private class AwsCredentialsFromSystem implements AWSCredentials {

        @Override
        public String getAWSAccessKeyId() {
            String accessKeyId = applicationProperties.getAwsAccessKey();
            if(accessKeyId == null || accessKeyId.isEmpty()) {
                throw new IllegalStateException("Mandatory sys property aws.accessKeyId not specified!");
            }
            return accessKeyId.trim();
        }

        @Override
        public String getAWSSecretKey() {
            String secretKey = applicationProperties.getAwsSecretKey();
            if(secretKey == null || secretKey.isEmpty()) {
                throw new IllegalStateException("Mandatory sys property aws.secretKey not specified!");
            }
            return secretKey.trim();
        }
        
    }
}
