# The Real Meta - Java Backend

## Environment Details
- Java 8
- Maven 3.3.9
- Spring 1.5.2

## Included from spring
- Actuator + docs
- Cloud connectors + Cloud Security (need to make sure compatible with platform)
- MongoDB
- REST Docs
- Security
- Web (For embedded Tomcat and Spring MVC)

- To add to our spring tool set: Swagger

# External Maven Dependencies
com.github.rithms | [riot-api-java Java Wrapper For API](https://github.com/taycaldwell/riot-api-java) | [Docs](http://taycaldwell.com/riot-api-java/doc/)

## Project Structure
```
com.meta.analyzer
    ├── Application.java		<= Startup Spring Boot
    ├── ApplicationProperties.java
    │ 
    ├── .riot.api.aggregator		<= Main controller for riot api calls 
    ├── .riot.api.grabber		<= Seperate method implementation for Riot Api calls
    │	 	├── MatchHistory	<= Get all info concering match history
    │	 	├── MatchItems		<= Get items based on overall match history, individual match, etc.
```

## Code Improvements and Refactoring Needed
- Champion Match History in MatchHistory is a map write now, probably better to do 2D ArrayList.

---
## In progress part of README

## Your Most Popular Build
Input: Summoner Name

(DONE) SummonerName -> SUMMONER-V3 /lol/summoner/v3/summoners/by-name/{summonerName} -> account id

(DONE) account id -> MATCH-V3 /lol/match/v3/matchlists/by-account/{accountId} -> List of gameIds

(DONE) gameIds -> MATCH-V3 /lol/match/v3/matches/{matchId} -> Map <ChampionId, gameIds> 

Map <ChampionId, gameIds> -> TODO -> Map <ChampionId, Items>

-> Aggregate ChampionId[item1%, item2%, ..., itemn%]
-> Display list of champions and their item%s
Output: Previously played champions and your item pick %

Putting to ES
		/*
		 * Index/Type/Id | Summoners/summoner/(Id) <= Id has to be unique for queries
		 * Entire Document: Summoner Name - ChampionId - MatchId -Items
		 */

## Extra Resources
# AWS resources
- [Using secret and access key for ES](http://mytechbites.blogspot.in/2017/04/secure-amazon-elastic-search-service.html)
- [Standard info on ES AWS](https://docs.aws.amazon.com/elasticsearch-service/latest/developerguide/what-is-amazon-elasticsearch-service.html)


## Application for Riot API prod key
Early Goal: Display a summoner's preferred build on different champions. 
Early Goal Ex. Voyboy builds protobelt on several champions, some unpopular, like akali. 
Search Voyboy's name and we can display his preferred build on akali. 
Also players can find their own builds and compare what they do differently to pros. 
Extra Goal: Display builds people win with that contain unpopular items. 
Extra Goal Ex. An irelia player wins consistently with tear and phantom dancer. 
Display this build to users. 
With enough aggregated data we can show highest win % off meta builds for as many champions as possible. 
Extra Notes: I separated the app. into an Angular2 node express front end and Java/Database back end. 
I have several reasons why, but the point is there will be two project URLs. 
https://github.com/JumpingRock/LeagueAnalyzer-Angular2 https://github.com/JumpingRock/League-Analyzer-Java-Backend 
I am still early into development, so the projects will be skeletons of what I am up to, I have yet to choose a database even. 

(Please recommend me one if you want!) Below is not relevant to the project, but I thought work mentioning: 
For the extra goal, Is there anyway I could work with someone to get a list of active ranked played below challenger/master? 
Otherwise my only idea is to store all the summoner names of people that use my application, and I can only display their builds. 
We worked with a list of users we could DISCOVER the hidden off-meta builds. Finally, any advice is welcome on architecture of the application!
If you need clarification on anything please let me know!

