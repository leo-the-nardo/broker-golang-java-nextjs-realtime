package com.leothenardo.homebroker.assets.listeners;


import lombok.ToString;

@ToString
public class PointReceivedDTO {
	private String symbol;
	private Float price;
	private int shares;

	public PointReceivedDTO() {
	}

	public PointReceivedDTO(String symbol, Float price, int shares) {
		this.symbol = symbol;
		this.price = price;
		this.shares = shares;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}
}