package com.meta.analyzer.elasticsearch;

import java.io.ByteArrayInputStream;
import java.net.URI;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.SdkBaseException;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.AmazonHttpClient.RequestExecutionBuilder;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpMethodName;
import com.meta.analyzer.ApplicationProperties;

@Component
public class SimpleES {
	@Autowired
	ApplicationProperties applicationProperties;
	
//	String accessKey = applicationProperties.getAwsAccessKey();
//	String secretKey = applicationProperties.getAwsSecretKey();
	
	String service_name = "es";
	String region = "us-east-1";
	String endpoint = "https://search-es-firebun-4m53ot3wf24fdmwvwc7jjziexa.us-east-1.es.amazonaws.com";
	
	/// Set up the request
	private Request<?> generateRequest(String reqBody) {
	       Request<?> request = new DefaultRequest<Object>(service_name);
	       request.setContent(new ByteArrayInputStream(reqBody.getBytes()));
	       request.setEndpoint(URI.create(endpoint + "/index/id"));
	       request.setHttpMethod(HttpMethodName.POST);
	       return request;
	}

	/// Perform Signature Version 4 signing
	private void performSigningSteps(Request<?> requestToSign) {
	       AWS4Signer signer = new AWS4Signer();
	       signer.setServiceName(service_name);
	       signer.setRegionName(region);      

	       // Get credentials
	       // NOTE: *Never* hard-code credentials
	       //       in source code
	       AWSCredentials creds = new BasicAWSCredentials(applicationProperties.getAwsAccessKey(), applicationProperties.getAwsSecretKey());

	       //AWSCredentials creds = credsProvider.getCredentials();

	       // Sign request with supplied creds
	       signer.sign(requestToSign, creds);
	}

	/// Send the request to the server
	private static void sendRequest(Request<?> request) {
	       ExecutionContext context = new ExecutionContext(true);

	       ClientConfiguration clientConfiguration = new ClientConfiguration();
	       AmazonHttpClient client = new AmazonHttpClient(clientConfiguration);
	       
	       // Initialize our handlers
	       SimpleHttpResponseHandler<?> responseHandler = new SimpleHttpResponseHandler<Void>();
	       SimpleErrorHandler<? extends SdkBaseException> errorHandler = new SimpleErrorHandler<>();
	       
	       // Build and execute our request
	       RequestExecutionBuilder requestBuilder = client.requestExecutionBuilder();
	       requestBuilder.executionContext(context);
	       requestBuilder.errorResponseHandler(errorHandler);
	       requestBuilder.request(request);
	       requestBuilder.execute(responseHandler);
	       
	       //Response<?> response =
	       //              client.execute(request, responseHandler, errorHandler, context);
	}
	
	//@PostConstruct
	public void testSimpleES() {
	       // Generate the request
	       Request<?> request = generateRequest("{\"field\":\"fieldValue\"}");

	       // Perform Signature Version 4 signing
	       performSigningSteps(request);

	       // Send the request to the server
	       sendRequest(request);
	}
	
	

}






