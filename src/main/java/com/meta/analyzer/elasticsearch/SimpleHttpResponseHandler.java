package com.meta.analyzer.elasticsearch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.util.IOUtils;

public class SimpleHttpResponseHandler<T> implements HttpResponseHandler<AmazonWebServiceResponse<T>> {

    @Override
    public AmazonWebServiceResponse<T> handle(
                     com.amazonaws.http.HttpResponse response) throws Exception {

            InputStream responseStream = response.getContent();
            String responseString = convertStreamToString(responseStream);
            System.out.println(responseString);
            
            AmazonWebServiceResponse<T> awsResponse = new AmazonWebServiceResponse<T>();
            return awsResponse;
    }

    private String convertStreamToString(InputStream responseStream) throws IOException {
    	String result = IOUtils.toString(responseStream);
		return result;
	}

	@Override
    public boolean needsConnectionLeftOpen() {
            return false;
    }
}