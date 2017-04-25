package com.SummonerController;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.constant.Region;
import net.rithms.riot.constant.Season;
import net.rithms.riot.dto.Stats.RankedStats;
import net.rithms.riot.dto.Summoner.Summoner;
import com.google.gson.*;


@Controller
@RequestMapping("/summonerSimple")
public class summonerController {

    private static final String template = "Hello, %s!";

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Summoner sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) throws RiotApiException {
    	
		RiotApi api = new RiotApi("RGAPI-a22eb6a6-8081-451a-941a-0b7cda5374a3", Region.NA);
		api.setRegion(Region.NA);
		Map<String, Summoner> summoners = api.getSummonersByName("firebun, TF Support bby");
		Summoner summoner = summoners.get("firebun");
		long id = summoner.getId();
		System.out.println(id);

    	
    	
        return summoner;
    }
}
