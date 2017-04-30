package com.meta.analyzer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.constant.Region;


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
	public RiotApi createApi() {
		RiotApi api = new RiotApi(applicationProperties.getApiKey(), Region.NA);
		api.setRegion(Region.NA);
		return api;
	}

}
