package com.leothenardo.homebroker.orders.listeners;

import java.util.List;

public class OrderDTO {
	private String order_id;
	private String investor_id;
	private String order_type;
	private String status;
	private int partial;
	private double price;
	private int shares;
	private List<TransactionDTO> transactions;

	public OrderDTO(String order_id, String investor_id, String order_type, String status, int partial, double price, int shares, List<TransactionDTO> transactions) {
		this.order_id = order_id;
		this.investor_id = investor_id;
		this.order_type = order_type;
		this.status = status;
		this.partial = partial;
		this.price = price;
		this.shares = shares;
		this.transactions = transactions;
	}

	public OrderDTO() {
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getInvestor_id() {
		return investor_id;
	}

	public void setInvestor_id(String investor_id) {
		this.investor_id = investor_id;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPartial() {
		return partial;
	}

	public void setPartial(int partial) {
		this.partial = partial;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public List<TransactionDTO> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionDTO> transactions) {
		this.transactions = transactions;
	}
}
