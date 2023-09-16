package com.leothenardo.homebroker.orders.model;

import com.leothenardo.homebroker.common.exceptions.EntityValidationException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
	@Field("id")
	private String id;

	@Field("shares")
	private int shares;

	@Field("price")
	private double price;

	@Field("buyer_id")
	private String buyerId;

	@Field("seller_id")
	private String sellerId;
	@Field("created_at")
	private LocalDateTime createdAt;

	public Transaction(String id, int shares, double price, String buyerId, String sellerId, LocalDateTime createdAt) {
		this.id = id;
		this.shares = shares;
		this.price = price;
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.createdAt = createdAt;
	}

	public Transaction() {
	}

	public static Transaction create(
					String id,
					int shares,
					double price,
					String buyerId,
					String sellerId
	) {
		var transaction = new Transaction(
						id,
						shares,
						price,
						buyerId,
						sellerId,
						LocalDateTime.now()
		);
		transaction.validate();
		return transaction;
	}

	public String getId() {
		return id;
	}

	public int getShares() {
		return shares;
	}

	public double getPrice() {
		return price;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	private void validate() {
		List<String> validationErrors = new ArrayList<>();
		if (this.id == null) {
			validationErrors.add("Id is required");
		}
		if (this.shares <= 0) {
			validationErrors.add("Shares must be greater than 0");
		}
		if (this.price <= 0) {
			validationErrors.add("Price must be greater than 0");
		}
		if (this.buyerId == null) {
			validationErrors.add("Buyer id is required");
		}
		if (this.sellerId == null) {
			validationErrors.add("Seller id is required");
		}
		if (!validationErrors.isEmpty()) {
			throw new EntityValidationException(validationErrors);
		}
	}
}
