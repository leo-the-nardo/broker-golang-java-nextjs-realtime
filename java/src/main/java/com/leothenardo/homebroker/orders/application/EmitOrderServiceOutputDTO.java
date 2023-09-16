package com.leothenardo.homebroker.orders.application;

import com.leothenardo.homebroker.orders.model.Order;
import com.leothenardo.homebroker.orders.model.OrderStatus;
import com.leothenardo.homebroker.orders.model.OrderType;

import java.time.LocalDateTime;

public record EmitOrderServiceOutputDTO(
				String id,
				String walletId,
				String assetId,
				int shares,
				double price,
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

