package com.meta.analyzer.controllers.simpleController;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meta.analyzer.jest.AggregateSummonerChampionsAndItems;
import com.meta.analyzer.jest.dto.ExtractedChampionItemCountDto;
import com.meta.analyzer.jest.dto.ExtractedItemTotalsDto;
import com.meta.analyzer.riot.api.aggregator.SimpleItemAggregator;

import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;


@Controller
@RequestMapping("/summonerSimple")
public class summonerController {
	
	@Autowired
	SimpleItemAggregator simpleItemAggregator;
    
	@Autowired
	AggregateSummonerChampionsAndItems aggregateSummonerChampionsAndItems;

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Summoner sayHello(@RequestParam(value="name", required=true) String summonerName) throws RiotApiException {
    	// Make sure match history is as up to date as possible
    	simpleItemAggregator.pullAndStoreSummonerData(summonerName);
    	
    	// Aggregate their matches!
    	ArrayList<ExtractedChampionItemCountDto> championItemCountList = aggregateSummonerChampionsAndItems.extractChampionsAndItems(summonerName);
    	ExtractedChampionItemCountDto championItemCount = championItemCountList.get(0);
    	championItemCount.getChampionId();
    	championItemCount.getGamesPlayedAsChampion();
    	ArrayList<ExtractedItemTotalsDto> itemTotalsList = championItemCount.getItemTotalsList();
    	itemTotalsList.get(0).getItemCount();
    	itemTotalsList.get(0).getItemId();
    	return null;
    }
    
}
