package com.meta.analyzer.controllers.simpleController;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;


@Controller
@RequestMapping("/summonerSimple")
public class summonerController {

    //private static final String template = "Hello, %s!";

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Summoner sayHello(@RequestParam(value="name", required=true) String name) throws RiotApiException {
    	
    	// MUST UPDATE USING NEW WRAPPER CURRENT CODE: v3.8.2 NEW SHOULD BE >v3.9.0
//		RiotApi api = new RiotApi("RGAPI-a22eb6a6-8081-451a-941a-0b7cda5374a3", Region.NA);
//		api.setRegion(Region.NA);
//		Map<String, Summoner> summoners = api.getSummonersByName(name);
//		Summoner summoner = summoners.get(name);
//		long id = summoner.getId();
//		System.out.println(id);
		
    	
    	
        //return summoner;
    	return null;
    }
    
}
