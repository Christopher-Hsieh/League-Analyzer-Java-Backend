package com.meta.analyzer.jest.dto;

public class ExtractedItemTotalsDto {
	int itemId;
	int itemCount;
	
	public ExtractedItemTotalsDto(int itemId, int itemCount) {
		this.itemId = itemId;
		this.itemCount = itemCount;
	}
	
	public int getItemId() {
		return itemId;
	}

	public int getItemCount() {
		return itemCount;
	}
}
