package com.meta.analyzer.elasticsearch;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Request;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.*;

public class SimpleES {
	/// Set up the request
	private static Request<?> generateRequest() {
	       Request<?> request = new DefaultRequest<Void>(SERVICE_NAME);
	       request.setContent(new ByteArrayInputStream("".getBytes()));
	       request.setEndpoint(URI.create(ENDPOINT));
	       request.setHttpMethod(HttpMethodName.GET);
	       return request;
	}

	/// Perform Signature Version 4 signing
	private static void performSigningSteps(Request<?> requestToSign) {
	       AWS4Signer signer = new AWS4Signer();
	       signer.setServiceName(SERVICE_NAME);
	       signer.setRegionName(REGION);      

	       // Get credentials
	       // NOTE: *Never* hard-code credentials
	       //       in source code
	       AWSCredentialsProvider credsProvider =
	                     new DefaultAWSCredentialsProviderChain();

	       AWSCredentials creds = credsProvider.getCredentials();

	       // Sign request with supplied creds
	       signer.sign(requestToSign, creds);
	}

	/// Send the request to the server
	private static void sendRequest(Request<?> request) {
	       ExecutionContext context = new ExecutionContext(true);

	       ClientConfiguration clientConfiguration = new ClientConfiguration();
	       AmazonHttpClient client = new AmazonHttpClient(clientConfiguration);

	       MyHttpResponseHandler<Void> responseHandler = new MyHttpResponseHandler<Void>();
	       MyErrorHandler errorHandler = new MyErrorHandler();

	       Response<Void> response =
	                     client.execute(request, responseHandler, errorHandler, context);
	}
}
