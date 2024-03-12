package com.leothenardo.homebroker.orders.application;

import com.leothenardo.homebroker.orders.entities.Order;
import com.leothenardo.homebroker.orders.entities.OrderStatus;
import com.leothenardo.homebroker.orders.entities.OrderType;

import java.time.LocalDateTime;

public record EmitOrderServiceOutputDTO(
				String id,
				String walletId,
				String symbol,
				int shares,
				Double price,
				OrderType type,
				OrderStatus status,
				int partial,
				LocalDateTime createdAt
) {
	public static EmitOrderServiceOutputDTO from(Order order) {
		return new EmitOrderServiceOutputDTO(
						order.getId(),
						order.getWalletId(),
						order.getAssetId(),
						order.getShares(),
						order.getPrice(),
						order.getType(),
						order.getStatus(),
						order.getPartial(),
						order.getCreatedAt()
		);
	}
}

