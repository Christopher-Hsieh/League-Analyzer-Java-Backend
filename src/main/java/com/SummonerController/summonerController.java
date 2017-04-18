package com.SummonerController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.DataRepresentations.Summoner;

@Controller
@RequestMapping("/summonerSimple")
public class summonerController {

    private static final String template = "Hello, %s!";

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Summoner sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
        return new Summoner(1, String.format(template, name));
    }
}
