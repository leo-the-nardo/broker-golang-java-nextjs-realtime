package com.leothenardo.homebroker.orders.application;

import com.leothenardo.homebroker.orders.model.OrderType;

public record OrderEmittedEventDTO(
				String order_id,
				String investor_id,
				String asset_id,
				int current_shares, // how hany shares of this asset the investor has
				int shares,
				double price,
				OrderType order_type
) {
}
