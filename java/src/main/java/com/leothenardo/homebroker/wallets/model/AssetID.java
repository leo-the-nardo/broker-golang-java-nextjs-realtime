package com.leothenardo.homebroker.wallets.model;

import java.util.Objects;
import java.util.UUID;

public class AssetID {
	private String value;

	public AssetID(String value) {
		Objects.requireNonNull(value);
		this.value = value;
	}

	public AssetID(UUID value) {
		Objects.requireNonNull(value);
		this.value = value.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AssetID assetID = (AssetID) o;
		return Objects.equals(value, assetID.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	public String toString() {
		return this.value;
	}
}
