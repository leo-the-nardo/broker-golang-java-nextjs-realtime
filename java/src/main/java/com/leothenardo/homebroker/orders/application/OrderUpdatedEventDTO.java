package com.leothenardo.homebroker.orders.application;

public record OrderUpdatedEventDTO(
				String orderId,
				String assetId,
				int shares,
				int negotiatedShares,
				double negotiatedPrice,
				double expectedPrice
) {

}
