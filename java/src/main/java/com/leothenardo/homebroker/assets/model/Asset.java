package com.leothenardo.homebroker.assets.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(Asset.COLLECTION_NAME)
public class Asset {
	public static final String COLLECTION_NAME = "assets";

	@Id
	@Field(value = "_id", targetType = FieldType.STRING)
	private String symbol;

	@Field("name")
	private String name;

	public Asset() {
	}

	public Asset(String symbol, String name) {
		this.symbol = symbol;
		this.name = name;
	}

	public static Asset create(String symbol, String name) {
		Objects.requireNonNull(symbol);
		Objects.requireNonNull(name);
		return new Asset(
						symbol,
						name
		);
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Asset asset = (Asset) o;
		return Objects.equals(symbol, asset.symbol);
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol);
	}


}