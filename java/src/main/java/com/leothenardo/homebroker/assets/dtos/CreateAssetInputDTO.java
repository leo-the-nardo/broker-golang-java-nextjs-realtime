package com.leothenardo.homebroker.assets.dtos;

import com.leothenardo.homebroker.assets.model.Asset;

public record CreateAssetInputDTO(String symbol, Float price) {
	public Asset toModel() {
		return Asset.create(symbol, price);
	}
}