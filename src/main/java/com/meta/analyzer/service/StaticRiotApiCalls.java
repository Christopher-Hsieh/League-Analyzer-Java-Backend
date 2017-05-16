package com.meta.analyzer.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meta.analyzer.jest.dto.ChampionItemCountDto;
import com.meta.analyzer.jest.dto.ItemDto;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.constant.Platform;

@Component
public class StaticRiotApiCalls {
	
	@Autowired
	RiotApi api;
	
	private Platform platform = Platform.NA;
	
	public String getChampionName(int championId)  {
		String championName = "";
		try {
			championName = api.getDataChampion(platform, championId).getName();
		} catch (RiotApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return championName;
	}
	
	public String getItemName(int id)  {
		String name = "";
		try {
			name = api.getDataItem(platform, id).getName();
		} catch (RiotApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
	
	public ObjectNode buildChampionItemsJson(ChampionItemCountDto dto)  {
		String championName = getChampionName(dto.getChampionId());
		ArrayList<ItemDto> items = dto.getItemTotalsList();
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode
                .put("name", championName)
                .put("gamesPlayed", dto.getGamesPlayedAsChampion())
                .put( getItemName(items.get(0).getItemId()), items.get(0).getItemCount())
                .put( getItemName(items.get(1).getItemId()), items.get(1).getItemCount())
                .put( getItemName(items.get(2).getItemId()), items.get(2).getItemCount())
                .put( getItemName(items.get(3).getItemId()), items.get(3).getItemCount())
                .put( getItemName(items.get(4).getItemId()), items.get(4).getItemCount())
                .put( getItemName(items.get(5).getItemId()), items.get(5).getItemCount())
                .put( getItemName(items.get(6).getItemId()), items.get(6).getItemCount());
		return objectNode;
	}
}
