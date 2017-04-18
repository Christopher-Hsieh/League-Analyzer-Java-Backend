package com.LeagueAnalyzerJavaBackend;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.constant.Region;
import net.rithms.riot.constant.Season;
import net.rithms.riot.dto.Stats.RankedStats;
import net.rithms.riot.dto.Summoner.Summoner;

import com.google.gson.*;

@SpringBootApplication
@ComponentScan("com.SummonerController")
public class Application {

	public static void main(String[] args) throws RiotApiException {
		SpringApplication.run(Application.class, args);
		
		RiotApi api = new RiotApi("RGAPI-6ec47d55-1394-4e4c-b524-738fe84e6da1", Region.NA);
		api.setRegion(Region.NA);
		Map<String, Summoner> summoners = api.getSummonersByName("rithms, tryndamere");
		Summoner summoner = summoners.get("rithms");
		long id = summoner.getId();
		System.out.println(id);
	}
}
