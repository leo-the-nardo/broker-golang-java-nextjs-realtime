package com.leothenardo.homebroker.orders.dtos;

import com.leothenardo.homebroker.orders.model.OrderType;

public record EmitOrderInputDTO(
				String assetId,
				OrderType type,
				int shares,
				double price
) {
}
