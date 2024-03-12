package com.leothenardo.homebroker.orders.listeners;

public class TransactionDTO {
	private String transaction_id;
	private String buyer_id;
	private String seller_id;
	private Double price;
	private int shares;

	public TransactionDTO() {
	}

	public TransactionDTO(String transaction_id, String buyer_id, String seller_id, Double price, int shares) {
		this.transaction_id = transaction_id;
		this.buyer_id = buyer_id;
		this.seller_id = seller_id;
		this.price = price;
		this.shares = shares;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}
}
