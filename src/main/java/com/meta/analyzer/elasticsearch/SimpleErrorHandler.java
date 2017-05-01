package com.meta.analyzer.elasticsearch;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.http.HttpResponseHandler;

public class SimpleErrorHandler<T> implements HttpResponseHandler<T> {

    @Override
    public T handle(
                     com.amazonaws.http.HttpResponse response) throws Exception {
            System.out.println("In exception handler!");

            AmazonServiceException ase = new AmazonServiceException("Fake service exception.");
            ase.setStatusCode(response.getStatusCode());
            ase.setErrorCode(response.getStatusText());
            return (T) ase;
      }

    @Override
    public boolean needsConnectionLeftOpen() {
            return false;
      }
}