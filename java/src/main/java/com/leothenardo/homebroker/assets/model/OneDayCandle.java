package com.leothenardo.homebroker.assets.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;

//This is a <- Materialized View -> Entity PostgresSQL + TimescaleDB
@Entity
public class OneDayCandle {

	@Id
	private Instant bucket;

	private String symbol;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Integer dayVolume;

	public OneDayCandle() {
	}

	public OneDayCandle(Instant bucket, String symbol, Double open, Double high, Double low, Double close, Integer dayVolume) {
		this.bucket = bucket;
		this.symbol = symbol;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.dayVolume = dayVolume;
	}

	public Instant getBucket() {
		return bucket;
	}

	public void setBucket(Instant bucket) {
		this.bucket = bucket;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Integer getDayVolume() {
		return dayVolume;
	}

	public void setDayVolume(Integer dayVolume) {
		this.dayVolume = dayVolume;
	}
}