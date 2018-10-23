package com.meta.analyzer.controllers.simpleController;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meta.analyzer.jest.AggregateChampionItems;
import com.meta.analyzer.jest.dto.ChampionItemCountDto;
import com.meta.analyzer.service.StaticRiotApiCalls;

import net.rithms.riot.api.RiotApiException;


@RestController
public class SimpleController {
	
	@Resource
	BlockingQueue<String> incomingSummonerQueue;
    
	@Autowired
	AggregateChampionItems aggregate;
	
	@Autowired
	StaticRiotApiCalls staticRiotApiCalls;


    @RequestMapping(value="/getBuilds", method=RequestMethod.GET)
    public @ResponseBody Object sayHello(@RequestParam(value="name", required=true) String summonerName) throws IOException  {
    	// Aggregate their matches!
    	
    	ChampionItemCountDto championItemList = aggregate.extractChampionItems(summonerName);
    	return staticRiotApiCalls.buildChampionItemsJson(championItemList);
    }
    
    @RequestMapping(value="/aggregateMatchData",method=RequestMethod.GET)
    public @ResponseBody String queueSummoner(@RequestParam(value="name", required=true) String summonerName) throws RiotApiException {
    	if (incomingSummonerQueue.contains(summonerName)) {
    		return "Name is already queued! Current Queue: " + incomingSummonerQueue.toString();
    	}
    	incomingSummonerQueue.add(summonerName);
    	return "Currently number " + incomingSummonerQueue.size() + " in Queue. Current Queue: " + incomingSummonerQueue.toString();
    }
    
//    public AggregateChampionItems getAggregateSummonerChampionsAndItems() {
//        return (AggregateChampionItems) context.getBean("aggregateChampionItems)");
//    }
}
