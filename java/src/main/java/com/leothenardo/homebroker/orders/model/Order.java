package com.leothenardo.homebroker.orders.model;

import com.leothenardo.homebroker.common.exceptions.EntityValidationException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.*;

@Document(Order.COLLECTION_NAME)
public class Order {
	public static final String COLLECTION_NAME = "orders";
	@Id
	@Field(value = "_id", targetType = FieldType.STRING)
	private String id;

	@Field("wallet_id")
	private String walletId;

	@Field("type")
	private OrderType type;

	@Field("asset_id")
	private String assetId;

	@Field("price")
	private double price;

	@Field("shares")
	private int shares;

	@Field("status")
	private OrderStatus status;

	@Field("partial")
	private int partial;

	@Field("transactions")
	private List<Transaction> transactions = new ArrayList<>();

	@Field("created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Field("updated_at")
	private LocalDateTime updatedAt;

	public Order() {
	}

	public Order(String id, String walletId, OrderType type, String assetId, double price, int shares, OrderStatus status, int partial, List<Transaction> transactions, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.walletId = walletId;
		this.type = type;
		this.assetId = assetId;
		this.price = price;
		this.shares = shares;
		this.status = status;
		this.partial = partial;
		this.transactions = transactions;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static Order create(String walletId, OrderType type, String assetId, double price, int shares) {
		var order = new Order(
						UUID.randomUUID().toString(),
						walletId,
						type,
						assetId,
						price,
						shares,
						OrderStatus.PENDING,
						shares,
						new ArrayList<>(),
						LocalDateTime.now(),
						null
		);
		order.validate();
		return order;
	}

	public void registerTransaction(Transaction transaction) {
		this.partial -= transaction.getShares();
		if (this.partial == 0) {
			this.status = OrderStatus.FULFILLED;
		}
		if (this.partial > 0) {
			this.status = OrderStatus.PARTIAL;
		}
		if (this.partial < 0) {
			this.status = OrderStatus.FAILED;
		}
		this.transactions.add(transaction);
	}

	public String getId() {
		return id;
	}

	public String getWalletId() {
		return walletId;
	}

	public OrderType getType() {
		return type;
	}

	public String getAssetId() {
		return assetId;
	}

	public double getPrice() {
		return price;
	}

	public int getShares() {
		return shares;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public int getPartial() {
		return partial;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	private void validate() {
		List<String> validationErrors = new ArrayList<>();
		if (this.id == null) {
			validationErrors.add("Id is required");
		}
		if (this.walletId == null) {
			validationErrors.add("WalletId is required");
		}
		if (this.type == null) {
			validationErrors.add("Type is required");
		}
		if (this.status == null) {
			validationErrors.add("Status is required");
		}
		if (this.partial < 0) {
			validationErrors.add("Partial must be positive");
		}
		if (this.shares <= 0) {
			validationErrors.add("Shares must be greater than 0");
		}
		if (this.price <= 0) {
			validationErrors.add("Price must be greater than 0");
		}
		if (this.assetId == null) {
			validationErrors.add("AssetId is required");
		}
		if (this.createdAt == null) {
			validationErrors.add("CreatedAt is required");
		}
		if (this.type != OrderType.BUY && this.type != OrderType.SELL) {
			validationErrors.add("Type must be BUY or SELL");
		}
		if (!validationErrors.isEmpty()) {
			throw new EntityValidationException(validationErrors);
		}
	}
	
}

