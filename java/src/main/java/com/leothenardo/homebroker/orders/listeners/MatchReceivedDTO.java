package com.leothenardo.homebroker.orders.listeners;


public class MatchReceivedDTO {
	private String transaction_id;
	private String asset_id;
	private int shares;
	private Double price;
	private OrderDTO buy_order;
	private OrderDTO sell_order;

	public MatchReceivedDTO() {
	}

	public MatchReceivedDTO(String transaction_id, String asset_id, int shares, Double price, OrderDTO buy_order, OrderDTO sell_order) {
		this.transaction_id = transaction_id;
		this.asset_id = asset_id;
		this.shares = shares;
		this.price = price;
		this.buy_order = buy_order;
		this.sell_order = sell_order;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getAsset_id() {
		return asset_id;
	}

	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public OrderDTO getBuy_order() {
		return buy_order;
	}

	public void setBuy_order(OrderDTO buy_order) {
		this.buy_order = buy_order;
	}

	public OrderDTO getSell_order() {
		return sell_order;
	}

	public void setSell_order(OrderDTO sell_order) {
		this.sell_order = sell_order;
	}
}