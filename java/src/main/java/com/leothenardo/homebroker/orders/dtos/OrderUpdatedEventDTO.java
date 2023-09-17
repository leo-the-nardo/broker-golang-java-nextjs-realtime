package com.leothenardo.homebroker.orders.dtos;

import com.leothenardo.homebroker.orders.model.Order;
import com.leothenardo.homebroker.orders.model.OrderStatus;
import com.leothenardo.homebroker.orders.model.OrderType;

public record OrderUpdatedEventDTO(
				String orderId,
				String assetId,
				int shares,
				int negotiatedShares,
				double negotiatedPrice,
				double expectedPrice,
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
