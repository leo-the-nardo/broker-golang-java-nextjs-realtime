package com.leothenardo.homebroker.orders.application;

import com.leothenardo.homebroker.orders.model.OrderType;

public record OrderEmittedEventDTO(
				String order_id,
				String investor_id,
				String asset_id,
				int shares,
				double price,
				OrderType order_type
) {
}
