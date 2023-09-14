package com.leothenardo.homebroker.orders.model;

import org.springframework.data.mongodb.core.mapping.Field;


public class AssetOnOrder {

	@Field("asset_id")
	private String assetId;

	@Field("price")
	private double price;

	@Field("shares")
	private int shares;

	public AssetOnOrder() {
	}

	public AssetOnOrder(String assetId, double price, int shares) {
		this.assetId = assetId;
		this.price = price;
		this.shares = shares;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}
}
