package com.meta.analyzer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

	@Value ("${riot.api.key}")
	private String apiKey;
	
	@Value ("${aws.access.key}")
	private String awsAccessKey;
	
	@Value ("${aws.secret.key}")
	private String awsSecretKey;
	
	@Value ("${es.endpoint}")
	private String esEndpoint;
	
	@Value ("${es.region}")
	private String esRegion;
	
	@Value ("${es.service.name}")
	private String esServiceName;
	
	public String getEsEndpoint() {
		return esEndpoint;
	}

	public String getEsRegion() {
		return esRegion;
	}

	public String getEsServiceName() {
		return esServiceName;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	public String getAwsAccessKey() {
		return awsAccessKey;
	}
}
