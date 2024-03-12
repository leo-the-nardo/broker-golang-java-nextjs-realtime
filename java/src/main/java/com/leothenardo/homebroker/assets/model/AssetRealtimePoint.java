package com.leothenardo.homebroker.assets.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Entity
@Table(name = "stocks_real_time", indexes = {
				@Index(name = "ix_symbol_time", columnList = "symbol, time DESC"),
				@Index(name = "stocks_real_time_time_idx", columnList = "time DESC")
})
public class AssetRealtimePoint {

	@Id
	private Instant time;

	@Field("symbol")
	private String symbol;

	@Field("price")
	private Float price;

	@Field("day_volume")
	private Integer dayVolume;

	public AssetRealtimePoint() {
	}

	public AssetRealtimePoint(Instant time, String symbol, Float price, Integer dayVolume) {
		this.time = time;
		this.symbol = symbol;
		this.price = price;
		this.dayVolume = dayVolume;
	}

	public static AssetRealtimePoint create(String symbol, Float price, Integer volume) {
		return new AssetRealtimePoint(Instant.now(), symbol, price, volume);
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
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

	public Integer getDayVolume() {
		return dayVolume;
	}

	public void setDayVolume(Integer dayVolume) {
		this.dayVolume = dayVolume;
	}
}