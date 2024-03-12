package com.leothenardo.homebroker.orders.dtos;

import com.leothenardo.homebroker.orders.entities.Order;
import com.leothenardo.homebroker.orders.entities.OrderStatus;
import com.leothenardo.homebroker.orders.entities.OrderType;

public record OrderUpdatedEventDTO(
				String orderId,
				String symbol,
				int shares,
				int negotiatedShares,
				Double negotiatedPrice,
				Double expectedPrice,
				OrderType type,
				OrderStatus status
) {

	public static OrderUpdatedEventDTO from(Order order) {
		return new OrderUpdatedEventDTO(
						order.getId(),
						order.getAssetId(),
						order.getShares(),
						order.getTransactions().get(order.getTransactions().size() - 1).getShares(),
						order.getTransactions().get(order.getTransactions().size() - 1).getPrice(),
						order.getPrice(),
						order.getType(),
						order.getStatus()
		);
	}
}
