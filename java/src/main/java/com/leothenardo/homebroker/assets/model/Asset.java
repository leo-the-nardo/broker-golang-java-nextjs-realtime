package com.leothenardo.homebroker.assets.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Document(Asset.COLLECTION_NAME)
public class Asset {
	public static final String COLLECTION_NAME = "assets";

	@Id
	@Field(value = "_id", targetType = FieldType.STRING)
	private String id;

	@Field("symbol")
	private String symbol;

	@Field("price")
	private Float price;

	@Field("created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Field("updated_at")
	private LocalDateTime updatedAt;

	public Asset() {
	}

	public Asset(String id, String symbol, Float price, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.symbol = symbol;
		this.price = price;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static Asset create(String symbol, Float price) {
		Objects.requireNonNull(symbol);
		Objects.requireNonNull(price);
		return new Asset(
						UUID.randomUUID().toString(),
						symbol,
						price,
						LocalDateTime.now(),
						null
		);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Asset asset = (Asset) o;
		return Objects.equals(id, asset.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}