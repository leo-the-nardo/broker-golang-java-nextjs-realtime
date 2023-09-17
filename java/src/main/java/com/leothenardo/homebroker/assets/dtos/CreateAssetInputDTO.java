package com.leothenardo.homebroker.assets.dtos;

import com.leothenardo.homebroker.assets.model.Asset;

public record CreateAssetInputDTO(String symbol, String name) {
	public Asset toModel() {
		return Asset.create(symbol, name);
	}
}