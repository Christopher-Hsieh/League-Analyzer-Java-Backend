package com.meta.analyzer;


import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.meta.analyzer.service.RequestProcessor;

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
	
	@Autowired
	RequestProcessor requestProcessor;
	
	
	@Bean(name = "riotApiLock")
	public Object riotApiLock(){
		return new Object();
	}

	@Bean
	public LinkedList<LocalDateTime> apiCallHistory() {
		LinkedList<LocalDateTime> apiCallHistory = new LinkedList<LocalDateTime>();
		return apiCallHistory;
	}
	
	@Bean
	public BlockingQueue<String> incomingSummonerQueue() {
		BlockingQueue<String> incomingSummonerQueue = new LinkedBlockingQueue<>();
		return incomingSummonerQueue;
	}
	
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
