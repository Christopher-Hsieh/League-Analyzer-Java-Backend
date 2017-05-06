package com.meta.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;


@SpringBootApplication
@PropertySource("classpath:/connections.properties")
@ComponentScan({"com.meta.analyzer.*","com.meta.analyzer"})
public class Application {

	public static void main(String[] args)  {
		SpringApplication.run(Application.class, args);
	}
	
	@Autowired
	ApplicationProperties applicationProperties;
	
	@Bean
	public ApiConfig apiConfig(){
		ApiConfig apiConfig = new ApiConfig();
		apiConfig.setKey(applicationProperties.getApiKey());
		apiConfig.setRespectRateLimit(true);
		return apiConfig;
	}
	
	@Bean
	public RiotApi createApi() {
		return new RiotApi(apiConfig());
	}

}
