package com.meta.analyzer.controllers.simpleController;

import java.util.ArrayList;
import java.util.Queue;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meta.analyzer.jest.AggregateSummonerChampionsAndItems;
import com.meta.analyzer.jest.dto.ExtractedChampionItemCountDto;

import net.rithms.riot.api.RiotApiException;


@RestController
public class summonerController {
	
	@Resource
	Queue<String> incomingSummonerQueue;
    
	@Autowired
	AggregateSummonerChampionsAndItems aggregateSummonerChampionsAndItems;

    @RequestMapping(value="/getBuilds", method=RequestMethod.GET)
    public @ResponseBody ExtractedChampionItemCountDto sayHello(@RequestParam(value="name", required=true) String summonerName) throws RiotApiException {
    	// Aggregate their matches!
    	ArrayList<ExtractedChampionItemCountDto> championItemCountList = aggregateSummonerChampionsAndItems.extractChampionsAndItems(summonerName);
    	ExtractedChampionItemCountDto championItemCount = championItemCountList.get(0);
//    	championItemCount.getChampionId();
//    	championItemCount.getGamesPlayedAsChampion();
//    	ArrayList<ExtractedItemTotalsDto> itemTotalsList = championItemCount.getItemTotalsList();
//    	itemTotalsList.get(0).getItemCount();
//    	itemTotalsList.get(0).getItemId();
    	return championItemCount;
    }
    
    @RequestMapping(value="/aggregateMatchData",method=RequestMethod.GET)
    public @ResponseBody int queueSummoner(@RequestParam(value="name", required=true) String summonerName) throws RiotApiException {
    	incomingSummonerQueue.add(summonerName);
    	return incomingSummonerQueue.size();
    }
    
}
