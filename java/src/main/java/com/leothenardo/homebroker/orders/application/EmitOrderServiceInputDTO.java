package com.leothenardo.homebroker.orders.application;

import com.leothenardo.homebroker.orders.model.OrderType;

public record EmitOrderServiceInputDTO(
				String walletId,
				String assetId,
				int shares,
				OrderType type,
				double price
) {
}
