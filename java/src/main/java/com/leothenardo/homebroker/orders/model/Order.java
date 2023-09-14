package com.leothenardo.homebroker.orders.model;

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

	@Field("status")
	private OrderStatus status;

	@Field("partial")
	private int partial;

	@CreatedDate
	@Field("created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Field("updated_at")
	private LocalDateTime updatedAt;

	@Field("asset")
	private AssetOnOrder assetOnOrder;


	public Order() {
	}

	public Order(String id, String walletId, OrderType type, OrderStatus status, int partial, LocalDateTime createdAt, LocalDateTime updatedAt, AssetOnOrder assetOnOrder) {
		this.id = id;
		this.walletId = walletId;
		this.type = type;
		this.status = status;
		this.partial = partial;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.assetOnOrder = assetOnOrder;
	}

	public static Order create(String walletId, OrderType type, AssetOnOrder assetOnOrder) {
		Objects.requireNonNull(walletId);
		Objects.requireNonNull(type);
		Objects.requireNonNull(assetOnOrder);
		return new Order(
						UUID.randomUUID().toString(),
						walletId,
						type,
						OrderStatus.PENDING,
						assetOnOrder.getShares(),
						LocalDateTime.now(),
						null,
						assetOnOrder
		);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public int getPartial() {
		return partial;
	}

	public void setPartial(int partial) {
		this.partial = partial;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public AssetOnOrder getAssetOnOrder() {
		return assetOnOrder;
	}

	public void setAssetOnOrder(AssetOnOrder assetOnOrder) {
		this.assetOnOrder = assetOnOrder;
	}
}

