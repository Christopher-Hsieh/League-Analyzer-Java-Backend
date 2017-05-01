package com.meta.analyzer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.constant.Region;

@Component
public class ApplicationProperties {

	@Value ("${riot.api.key}")
	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

}