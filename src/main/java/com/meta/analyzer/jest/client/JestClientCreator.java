package com.meta.analyzer.jest.client;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.google.common.base.Supplier;
import com.meta.analyzer.ApplicationProperties;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

@Component
public class JestClientCreator {
	
	@Autowired
	ApplicationProperties applicationProperties;
	
	/*
	 * Only class someone should call to get the Jest Client. Signed by AWS4Signer
	 */
	public JestClient getJestClient() {

	    final JestClientFactory factory = getJestClientFactory();

	    factory.setHttpClientConfig(
	    		new HttpClientConfig
	            	.Builder(applicationProperties.getEsEndpoint())
	            	.multiThreaded(true)
	            	.build());
	    
	    return factory.getObject();
	}
	
	private JestClientFactory getJestClientFactory() {

	    return new JestClientFactory() {

	        @Override
	        protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
	            builder.addInterceptorLast(prepareInterceptor());
	            return builder;
	        }

	        @Override
	        protected HttpAsyncClientBuilder configureHttpClient(HttpAsyncClientBuilder builder) {
	            builder.addInterceptorLast(prepareInterceptor());
	            return builder;
	        }
	    };
	}
	
	// Creates Interceptor so HTTP call knows it's AWS signed
	private AWSSigningRequestInterceptor prepareInterceptor() {
		final Supplier<LocalDateTime> clock = () -> LocalDateTime.now(ZoneOffset.UTC);
		final AWSSigner awsSigner = new AWSSigner(createCredentialsProvider(), applicationProperties.getEsRegion(), applicationProperties.getEsServiceName(), clock);
	    return new AWSSigningRequestInterceptor(awsSigner);
	}
	
	// Simply wrap credentials into a provider
	private AWSCredentialsProvider createCredentialsProvider() {
		return new AWSStaticCredentialsProvider(new AwsCredentialsFromSystem());
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
